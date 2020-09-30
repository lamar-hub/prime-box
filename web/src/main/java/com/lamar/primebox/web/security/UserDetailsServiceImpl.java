package com.lamar.primebox.web.security;

import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.model.UserCredentials;
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
            final UserCredentialsDto userCredentialsDto = userService.getUserCredentials(username);
            if (userCredentialsDto == null) {
                throw new Exception("user not found");
            }

            return UserCredentials.builder()
                                  .userId(userCredentialsDto.getUserId())
                                  .username(userCredentialsDto.getUsername())
                                  .password(userCredentialsDto.getPassword())
                                  .active(userCredentialsDto.isActive())
                                  .twoFactorVerification(userCredentialsDto.isTwoFactorVerification())
                                  .build();
        } catch (Exception exception) {
            log.error("user not found", exception);
            throw new UsernameNotFoundException("user not found");
        }
    }
}
