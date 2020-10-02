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
                throw new Exception("user not found");
            }

            return User.builder()
                       .userId(userDto.getUserId())
                       .email(userDto.getEmail())
                       .password(userDto.getPassword())
                       .name(userDto.getName())
                       .surname(userDto.getSurname())
                       .stored(userDto.getStored())
                       .limit(userDto.getLimit())
                       .active(userDto.isActive())
                       .twoFactorVerification(userDto.isTwoFactorVerification())
                       .build();
        } catch (Exception exception) {
            log.error("user not found", exception);
            throw new UsernameNotFoundException("user not found");
        }
    }
}
