package com.vinay.service;

import com.vinay.exception.ProductException;
import com.vinay.model.Cart;
import com.vinay.model.CartItem;
import com.vinay.model.User;
import com.vinay.request.AddItemRequest;

public interface CartService {
	public Cart createCart(User user);
	public CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException;
	public Cart findUserCart(Long userId);
}
