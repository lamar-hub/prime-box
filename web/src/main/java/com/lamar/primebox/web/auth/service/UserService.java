package com.lamar.primebox.web.auth.service;

import com.lamar.primebox.web.auth.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void validateEmail(User user) throws Exception;

    void addUser(User User);

    void authenticate(User user) throws Exception;

    void generateToken(User user);

    String getUserIDFromToken(String token);

    User updateUserPlan(String userID) throws Exception;

    void deleteUser(String userID) throws Exception;

}
