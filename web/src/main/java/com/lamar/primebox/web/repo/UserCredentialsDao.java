package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.UserCredentials;

public interface UserCredentialsDao {

    UserCredentials getByUsername(String username);

}
