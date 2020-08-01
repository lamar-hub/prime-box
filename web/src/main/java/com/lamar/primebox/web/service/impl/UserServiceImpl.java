package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.email.dto.SendNotificationDto;
import com.lamar.primebox.email.manager.NotificationManager;
import com.lamar.primebox.email.model.NotificationType;
import com.lamar.primebox.web.dto.model.UserAndJwtDto;
import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.repo.UserDao;
import com.lamar.primebox.web.service.UserService;
import com.lamar.primebox.web.util.CapacityUtil;
import com.lamar.primebox.web.util.EmailUtil;
import com.lamar.primebox.web.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
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
    private final NotificationManager notificationManager;

    public UserServiceImpl(@Lazy AuthenticationManager authenticationManager,
                           UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           JwtUtil jwtUtil,
                           CapacityUtil capacityUtil,
                           NotificationManager notificationManager) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.capacityUtil = capacityUtil;
        this.notificationManager = notificationManager;
    }

    @Override
    @Transactional
    public UserDto getUser(String username) throws Exception {
        final User user = userDao.getByUsername(username);
        if (user != null) {
            return modelMapper.map(user, UserDto.class);
        }
        throw new Exception("Exception! User was not found!");
    }

    @Override
    @Transactional
    public UserDto addUser(UserBasicDto userBasicDto) throws Exception {
        final User user = new User()
                .setEmail(userBasicDto.getUsername())
                .setPassword(passwordEncoder.encode(userBasicDto.getPassword()))
                .setName(userBasicDto.getName())
                .setSurname(userBasicDto.getSurname())
                .setStored(capacityUtil.getDefaultStored())
                .setLimit(capacityUtil.getLimit(Optional.empty()));

        userDao.saveUser(user);

        final SendNotificationDto sendNotificationDto = createActivateSendEmailDto(user);
        notificationManager.queueNotification(sendNotificationDto);
        
        return modelMapper.map(user, UserDto.class);
    }

    private SendNotificationDto createActivateSendEmailDto(User user) {
        final Map<String, String> templateModel = new HashMap<>();
        templateModel.put("activationUrl", EmailUtil.createActivationUrl(user.getEmail(), "12345"));
        
        final SendNotificationDto sendNotificationDto = new SendNotificationDto()
                .setNotificationTo(user.getEmail())
                .setNotificationType(NotificationType.EMAIL_ACTIVATION)
                .setTemplateModel(Map.of());
        return sendNotificationDto;
    }

    @Override
    @Transactional
    public UserAndJwtDto authenticateUser(UserCredentialsDto userCredentialsDto) throws Exception {
        authenticate(userCredentialsDto.getUsername(), userCredentialsDto.getPassword());
        final User user = userDao.getByUsername(userCredentialsDto.getUsername());
        if (user != null) {
            final String jwtToken = jwtUtil.generateToken(user.getUserId(), user.getUsername());
            return modelMapper.map(user, UserAndJwtDto.class).setJwtToken(jwtToken);
        }
        throw new Exception("Exception! User was not authenticated!");
    }

    private void authenticate(String username, String password) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserBasicDto userBasicDto) throws Exception {
        final User user = userDao.getByUsername(userBasicDto.getUsername());
        if (user != null) {
            user.setPassword(passwordEncoder.encode(userBasicDto.getPassword()))
                    .setName(userBasicDto.getName())
                    .setSurname(userBasicDto.getSurname());
            return modelMapper.map(user, UserDto.class);
        }
        throw new Exception("Exception! User was not updated!");
    }

    @Override
    @Transactional
    public UserDto deactivateUser(String username) throws Exception {
        final User user = userDao.getByUsername(username);
        if (user != null) {
            user.setActive(false);
            return modelMapper.map(user, UserDto.class);
        }
        throw new Exception("Exception! User was not deactivated!");
    }

}
