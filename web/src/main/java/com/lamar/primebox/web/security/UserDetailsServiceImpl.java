package com.lamar.primebox.web.security;

import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            final UserDto userDto = userService.getUser(username);
            if (userDto == null) {
                throw new Exception();
            }
            return new User(userDto.getEmail(), userDto.getPassword(), new ArrayList<>());
        } catch (Exception e) {
            throw new UsernameNotFoundException("User was not found!");
        }
    }
}
