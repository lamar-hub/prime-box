package com.lamar.primebox.web.security;

import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            return new User()
                    .setUserId(userDto.getUserId())
                    .setEmail(userDto.getEmail())
                    .setPassword(userDto.getPassword())
                    .setName(userDto.getName())
                    .setSurname(userDto.getSurname())
                    .setStored(userDto.getStored())
                    .setLimit(userDto.getLimit())
                    .setActive(userDto.isActive());
        } catch (Exception e) {
            throw new UsernameNotFoundException("User was not found!");
        }
    }
}
