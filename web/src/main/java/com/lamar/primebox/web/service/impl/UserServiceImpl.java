package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserJwtDto;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.repo.UserDao;
import com.lamar.primebox.web.service.UserService;
import com.lamar.primebox.web.util.CapacityUtil;
import com.lamar.primebox.web.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public UserServiceImpl(AuthenticationManager authenticationManager, UserDao userDao, PasswordEncoder passwordEncoder, ModelMapper modelMapper, JwtUtil jwtUtil, CapacityUtil capacityUtil) {
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
        if (username != null) {
            User user = userDao.getByUsername(username);
            return modelMapper.map(user, UserDto.class);
        }
        throw new Exception("Exception! User was not found!");
    }

    @Override
    @Transactional
    public UserDto addUser(UserBasicDto userBasicDto) throws Exception {
        User user = userDao.getByUsername(userBasicDto.getUsername());
        if (user == null) {
            user = new User()
                    .setEmail(userBasicDto.getUsername())
                    .setPassword(passwordEncoder.encode(userBasicDto.getPassword()))
                    .setName(userBasicDto.getName())
                    .setSurname(userBasicDto.getSurname())
                    .setStored(capacityUtil.getDefaultStored())
                    .setLimit(capacityUtil.getLimit(Optional.empty()));
            return modelMapper.map(user, UserDto.class);
        }
        throw new Exception("Exception! User was not created!");
    }

    @Override
    @Transactional
    public UserJwtDto authenticateUser(UserCredentialsDto userCredentialsDto) throws Exception {
        authenticate(userCredentialsDto.getUsername(), userCredentialsDto.getPassword());
        User user = userDao.getByUsername(userCredentialsDto.getUsername());
        if (user != null) {
            String jwtToken = jwtUtil.generateToken(user.getUserID(), user.getUsername());
            return new UserJwtDto().setJwtToken(jwtToken);
        }
        throw new Exception("Exception! User was not authenticated!");
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserBasicDto userBasicDto) throws Exception {
        User user = userDao.getByUsername(userBasicDto.getUsername());
        if (user != null) {
            user.setName(userBasicDto.getName())
                    .setSurname(userBasicDto.getSurname());
            return modelMapper.map(user, UserDto.class);
        }
        throw new Exception("Exception! User was not updated!");
    }

    @Override
    @Transactional
    public UserDto deactivateUser(String username) throws Exception {
        User user = userDao.getByUsername(username);
        if (userDao.deleteUser(user.getUserID()) == 1) {
            return modelMapper.map(user, UserDto.class);
        }
        throw new Exception("Exception! User was not updated!");
    }

}
