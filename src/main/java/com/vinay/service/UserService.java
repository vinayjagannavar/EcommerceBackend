package com.vinay.service;

import java.util.List;

import com.vinay.exception.UserException;
import com.vinay.model.User;

public interface UserService {
	public User findUserById(Long userId) throws UserException;
	
	public User findUserByJwt(String jwt) throws UserException;
	
	public List<User> findAllUsers();
}
