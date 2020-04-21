package com.lamar.primebox.web.auth.dao;

import com.lamar.primebox.web.auth.entity.User;

public interface UserDAO {

    User saveUser(User user);

    User getUser(String id);

    User getByUsername(String username);

    User updateUser(User user);

    int deleteUser(String userID);

    User existUser(String email, String password);
}
