package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserUpdateDto;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.repo.UserDao;
import com.lamar.primebox.web.service.UserService;
import com.lamar.primebox.web.util.CapacityUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final CapacityUtil capacityUtil;

    public UserServiceImpl(UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           @Qualifier("webModelMapper") ModelMapper modelMapper,
                           CapacityUtil capacityUtil) {
        this.userDao = userDao;
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
    public UserDto addUser(UserBasicDto userBasicDto) {
        final User user = User.builder()
                              .email(userBasicDto.getUsername())
                              .phone(userBasicDto.getPhone())
                              .name(userBasicDto.getName())
                              .surname(userBasicDto.getSurname())
                              .stored(capacityUtil.getDefaultStored())
                              .limit(capacityUtil.getLimit(Optional.empty()))
                              .password(passwordEncoder.encode(userBasicDto.getPassword()))
                              .active(false)
                              .twoFactorVerification(false)
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

        user.setPhone(userUpdateDto.getPhone())
            .setName(userUpdateDto.getName())
            .setSurname(userUpdateDto.getSurname())
            .setActive(userUpdateDto.isActive())
            .setTwoFactorVerification(userUpdateDto.isTwoFactorVerification());
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUserPlan(String username) throws Exception {
        final User user = userDao.getByEmail(username);

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }
        
        user.setLimit(capacityUtil.getLimit(Optional.of(user.getLimit())));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto activateUser(String username) throws Exception {
        final User user = userDao.getByEmail(username);

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        user.setActive(true);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto deactivateUser(String username) throws Exception {
        final User user = userDao.getByEmail(username);

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        user.setActive(false);
        return modelMapper.map(user, UserDto.class);
    }

}
