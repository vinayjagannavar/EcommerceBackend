package com.vinay.service;

import java.util.List;

import com.vinay.exception.ProductException;
import com.vinay.model.Rating;
import com.vinay.model.User;
import com.vinay.request.RatingRequest;

public interface RatingService {
	public Rating createRating(RatingRequest req, User user) throws ProductException;
	public List<Rating> getProductRating(Long productId);
}
