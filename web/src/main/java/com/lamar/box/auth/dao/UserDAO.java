package com.lamar.box.auth.dao;

import com.lamar.box.auth.entity.User;

public interface UserDAO {
	
	public User saveUser(User user);
	
	public User getUser(String id);
	
	public User getByUsername(String username);
	
	public User updateUser(User user);
	
	public int deleteUser(String userID);
	
	public User existUser(String email, String password);
}
