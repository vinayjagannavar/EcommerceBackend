package com.vinay.service;

import java.time.LocalDateTime;
import java.util.List;

import com.vinay.exception.ProductException;
import com.vinay.model.Product;
import com.vinay.model.Review;
import com.vinay.model.User;
import com.vinay.repository.ReviewRepository;
import com.vinay.request.ReviewRequest;

public class ReviewServiceImplementation implements ReviewService{
	
	private ProductService productService;
	private ReviewRepository reviewRepository;
	

	public ReviewServiceImplementation(ProductService productService, ReviewRepository reviewRepository) {
		super();
		this.productService = productService;
		this.reviewRepository = reviewRepository;
	}

	@Override
	public Review createReview(ReviewRequest req, User user) throws ProductException {
		Product product = productService.findProductById(req.getProductId());
		Review review = new Review();
		review.setProduct(product);
		review.setUser(user);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
	
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllReview(Long productId) {
		return reviewRepository.getAllProductReviews(productId);
	}

}
