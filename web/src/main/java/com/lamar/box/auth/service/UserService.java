package com.lamar.box.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.lamar.box.auth.entity.User;

public interface UserService extends UserDetailsService {
	
	public void validateEmail(User user) throws Exception;

	public void addUser(User User);
	
	public void authenticate(User user) throws Exception;
	
	public void generateToken(User user);
	
	public String getUserIDFromToken(String token);
	
	public User updateUserPlan(String userID) throws Exception;
	
	public void deleteUser(String userID) throws Exception;
	
}
