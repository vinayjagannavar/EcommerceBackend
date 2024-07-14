package com.vinay.service;

import java.time.LocalDateTime;
import java.util.List;

import com.vinay.exception.ProductException;
import com.vinay.model.Product;
import com.vinay.model.Rating;
import com.vinay.model.User;
import com.vinay.repository.RatingRepository;
import com.vinay.request.RatingRequest;

public class RatingServiceImplementation implements RatingService{

	private RatingRepository ratingRepository;
	private ProductService productService;
	
	
	public RatingServiceImplementation(RatingRepository ratingRepository, ProductService productService) {
		super();
		this.ratingRepository = ratingRepository;
		this.productService = productService;
	}

	@Override
	public Rating createRating(RatingRequest req, User user) throws ProductException {
		Product product = productService.findProductById(req.getProductId());
		Rating rating = new Rating();
		rating.setProduct(product);
		rating.setUser(user);
		rating.setRating(req.getRating());
		rating.setCreatedAt(LocalDateTime.now());
		return ratingRepository.save(rating);
	}

	@Override
	public List<Rating> getProductRating(Long productId) {
		
		return ratingRepository.getAllProductRating(productId);
	}

}
