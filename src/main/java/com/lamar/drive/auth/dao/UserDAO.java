package com.lamar.drive.auth.dao;

import com.lamar.drive.auth.entity.User;

public interface UserDAO {
	
	public User saveUser(User user);
	
	public User getUser(String id);
	
	public User getByUsername(String username);
	
	public User updateUser(User user);
	
	public int deleteUser(String userID);
	
	public User existUser(String email, String password);
}
