package com.vinay.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.vinay.exception.OrderException;
import com.vinay.exception.UserException;
import com.vinay.model.Order;
import com.vinay.repository.OrderRepository;
import com.vinay.response.ApiResponse;
import com.vinay.response.PaymentLinkResponse;
import com.vinay.service.OrderService;
import com.vinay.service.PaypalService;
import com.vinay.service.UserService;
import com.vinay.user.domain.OrderStatus;
import com.vinay.user.domain.PaymentStatus;

@RestController
@RequestMapping("/api")
public class PaypalController {

    @Value("${paypal.client.success-url}")
    private String successUrl;

    @Value("${paypal.client.cancel-url}")
    private String cancelUrl;

    private final OrderService orderService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final PaypalService payPalService;

    public PaypalController(OrderService orderService, UserService userService, OrderRepository orderRepository, PaypalService payPalService) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.payPalService = payPalService;
    }

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)
            throws UserException, OrderException, PayPalRESTException {

        Order order = orderService.findOrderById(orderId);
        try {
            Payment payment = payPalService.createPayment(order.getTotalPrice(), "USD", "paypal",
                    "sale", "Order Payment", cancelUrl, successUrl + "" + orderId);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    PaymentLinkResponse res = new PaymentLinkResponse(link.getHref(), payment.getId());
                    order.setOrderId(payment.getId());
                    orderRepository.save(order);
                    return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (PayPalRESTException e) {
            System.out.println("Error creating payment link: " + e.getMessage());
            throw new PayPalRESTException(e.getMessage());
        }
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect(@RequestParam(name = "payment_id") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam("order_id") Long orderId)
            throws PayPalRESTException, OrderException {
    	System.out.println("Payments: "+ paymentId +" "+ payerId + " "+ orderId);
        Order order = orderService.findOrderById(orderId);
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            System.out.println("payment details --- " + payment + payment.getState());

            if (payment.getState().equals("approved")) {
                order.getPaymentDetails().setPaymentId(paymentId);
                order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
                order.setOrderStatus(OrderStatus.PLACED);
                orderRepository.save(order);
                ApiResponse res = new ApiResponse("Your order has been placed successfully", true);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error fetching payment details: " + e.getMessage());
            new RedirectView("https://shopwithzosh.vercel.app/payment/failed");
            throw new PayPalRESTException(e.getMessage());
        }
        ApiResponse res = new ApiResponse("Payment failed", false);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
