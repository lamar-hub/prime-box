package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserUpdateDto;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.model.UserCredentials;
import com.lamar.primebox.web.repo.UserCredentialsDao;
import com.lamar.primebox.web.repo.UserDao;
import com.lamar.primebox.web.service.UserService;
import com.lamar.primebox.web.util.CapacityUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserCredentialsDao userCredentialsDao;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final CapacityUtil capacityUtil;

    public UserServiceImpl(UserDao userDao,
                           UserCredentialsDao userCredentialsDao, PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           CapacityUtil capacityUtil) {
        this.userDao = userDao;
        this.userCredentialsDao = userCredentialsDao;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.capacityUtil = capacityUtil;
    }

    @Override
    @Transactional
    public UserDto getUser(String username) throws Exception {
        final User user = userDao.getByEmail(username);

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserCredentialsDto getUserCredentials(String username) throws Exception {
        final UserCredentials userCredentials = userCredentialsDao.getByUsername(username);

        if (userCredentials == null) {
            log.error("user credentials not found");
            throw new Exception("user credentials not found");
        }

        return modelMapper.map(userCredentials, UserCredentialsDto.class);
    }

    @Override
    @Transactional
    public UserDto addUser(UserBasicDto userBasicDto) {
        final User user = User.builder()
                              .email(userBasicDto.getUsername())
                              .name(userBasicDto.getName())
                              .surname(userBasicDto.getSurname())
                              .stored(capacityUtil.getDefaultStored())
                              .limit(capacityUtil.getLimit(Optional.empty()))
                              .userCredentials(
                                      UserCredentials.builder()
                                                     .username(userBasicDto.getUsername())
                                                     .password(passwordEncoder.encode(userBasicDto.getPassword()))
                                                     .build()
                                              )
                              .build();

        userDao.saveUser(user);

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserUpdateDto userUpdateDto) throws Exception {
        final User user = userDao.getByEmail(userUpdateDto.getUsername());

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        user.setName(userUpdateDto.getName())
            .setSurname(userUpdateDto.getSurname())
            .getUserCredentials()
            .setPassword(passwordEncoder.encode(userUpdateDto.getPassword()))
            .setTwoFactorVerification(userUpdateDto.isTwoFactorVerification());
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserCredentialsDto deactivateUser(String username) throws Exception {
        final UserCredentials userCredentials = userCredentialsDao.getByUsername(username);

        if (userCredentials == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        userCredentials.setActive(false);
        return modelMapper.map(userCredentials, UserCredentialsDto.class);
    }

}
