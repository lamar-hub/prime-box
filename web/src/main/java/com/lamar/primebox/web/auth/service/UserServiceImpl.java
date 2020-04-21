package com.lamar.primebox.web.auth.service;

import com.lamar.primebox.web.auth.dao.UserDAO;
import com.lamar.primebox.web.auth.entity.User;
import com.lamar.primebox.web.auth.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(AuthenticationManager authenticationManager, UserDAO userDAO, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void validateEmail(User user) {
    }

    @Override
    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("USER_NOT_FOUND '" + username + "'");
        }
        return user;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStored(0L);
        user.setLimit(500000000L);
        userDAO.saveUser(user);
    }

    @Override
    public void authenticate(User user) throws Exception {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        } catch (DisabledException e) {
            throw new Exception("User disabled exception!", e);
        } catch (Exception e) {
            throw new Exception("Invalid credentials", e);
        }
    }

    @Override
    public void generateToken(User user) {
        user.setToken(jwtUtil.generateToken(user));
        user.setExpireIn(1000 * 60 * 60);
    }

    @Override
    @Transactional
    public User updateUserPlan(String userID) throws Exception {
        User user = userDAO.getUser(userID);
        if (user.getLimit() == 500000000L) {
            user.setLimit(5000000000L);
        } else {
            if (user.getLimit() == 5000000000L) {
                user.setLimit(100000000000L);
            } else {
                throw new Exception("Invalid upgrading");
            }
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(String userID) throws Exception {
        int deleted = userDAO.deleteUser(userID);
        if (deleted != 1) {
            throw new Exception("Invalid deleting");
        }
    }

    @Override
    public String getUserIDFromToken(String token) {
        return this.jwtUtil.getUserIDFromToken(token.split(" ")[1]);
    }

}
