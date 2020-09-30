package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.User;

public interface UserDao {

    void saveUser(User user);

    User getUser(String id);

    User getByEmail(String email);
}
