package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.web.dto.model.UserAndJwtDto;
import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.repo.UserDao;
import com.lamar.primebox.web.service.UserService;
import com.lamar.primebox.web.util.CapacityUtil;
import com.lamar.primebox.web.util.JwtUtil;
import com.lamar.primebox.web.util.StorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final CapacityUtil capacityUtil;

    public UserServiceImpl(@Lazy AuthenticationManager authenticationManager,
                           UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           JwtUtil jwtUtil,
                           CapacityUtil capacityUtil) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.capacityUtil = capacityUtil;
    }

    @Override
    @Transactional
    public UserDto getUser(String username) throws Exception {
        final User user = userDao.getByUsername(username);

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto addUser(UserBasicDto userBasicDto) {
        final User user = new User()
                .setEmail(userBasicDto.getUsername())
                .setPassword(passwordEncoder.encode(userBasicDto.getPassword()))
                .setName(userBasicDto.getName())
                .setSurname(userBasicDto.getSurname())
                .setStored(capacityUtil.getDefaultStored())
                .setLimit(capacityUtil.getLimit(Optional.empty()));

        userDao.saveUser(user);

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserAndJwtDto authenticateUser(UserCredentialsDto userCredentialsDto) throws Exception {
        authenticate(userCredentialsDto.getUsername(), userCredentialsDto.getPassword());
        final User user = userDao.getByUsername(userCredentialsDto.getUsername());

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        final UserAndJwtDto userAndJwtDto = modelMapper.map(user, UserAndJwtDto.class);

        if (!user.isTwoFactorVerification()) {
            final String jwtToken = jwtUtil.generateToken(user.getUserId(), user.getUsername());
            userAndJwtDto.setJwtToken(jwtToken);

            return userAndJwtDto;
        }

        user.setVerificationCode(StorageUtil.randomCode());
        return userAndJwtDto;
    }

    private void authenticate(String username, String password) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    @Transactional
    public UserAndJwtDto authenticateVerificationCodeUser(UserCredentialsDto userCredentialsDto) throws Exception {
        authenticate(userCredentialsDto.getUsername(), userCredentialsDto.getPassword());
        final User user = userDao.getByUsername(userCredentialsDto.getUsername());

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        if (!userCredentialsDto.getVerificationCode().equals(user.getVerificationCode())) {
            log.error("user not found");
            throw new Exception("verification code not match");
        }

        final String jwtToken = jwtUtil.generateToken(user.getUserId(), user.getUsername());

        return modelMapper.map(user, UserAndJwtDto.class).setJwtToken(jwtToken);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserBasicDto userBasicDto) throws Exception {
        final User user = userDao.getByUsername(userBasicDto.getUsername());

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        user.setPassword(passwordEncoder.encode(userBasicDto.getPassword()))
                .setName(userBasicDto.getName())
                .setSurname(userBasicDto.getSurname());
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto deactivateUser(String username) throws Exception {
        final User user = userDao.getByUsername(username);

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        user.setActive(false);
        return modelMapper.map(user, UserDto.class);
    }

}
