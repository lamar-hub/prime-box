package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.User;

public interface UserDao {

    User saveUser(User user);

    User getUser(String id);

    User getByUsername(String username);

    User updateUser(User user);

    int deleteUser(String userID);

    User existUser(String email, String password);
}