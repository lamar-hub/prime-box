package com.lamar.primebox.web.controller;

import com.lamar.primebox.notification.dto.SendNotificationDto;
import com.lamar.primebox.notification.manager.NotificationManager;
import com.lamar.primebox.notification.model.NotificationType;
import com.lamar.primebox.web.dto.model.UserAndJwtDto;
import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.request.UserLogInRequest;
import com.lamar.primebox.web.dto.request.UserLogInVerificationCodeRequest;
import com.lamar.primebox.web.dto.request.UserSignUpRequest;
import com.lamar.primebox.web.dto.response.UserLogInResponse;
import com.lamar.primebox.web.dto.response.UserSignUpResponse;
import com.lamar.primebox.web.service.UserService;
import com.lamar.primebox.web.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH})
@Slf4j
public class AuthController {

    private final UserService userService;
    private final NotificationManager notificationManager;
    private final ModelMapper modelMapper;

    public AuthController(UserService userService, NotificationManager notificationManager, ModelMapper modelMapper) {
        this.userService = userService;
        this.notificationManager = notificationManager;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponse> addUser(@RequestBody @Valid UserSignUpRequest signUpRequest) throws Exception {
        final UserBasicDto userBasicDto = modelMapper.map(signUpRequest, UserBasicDto.class);
        final UserDto userDto = userService.addUser(userBasicDto);

        final SendNotificationDto sendNotificationDto = createActivateSendEmailDto(userDto);
        try {
            notificationManager.queueNotification(sendNotificationDto);
        } catch (Exception exception) {
            log.error("notification error", exception);
        }

        final UserSignUpResponse signUpResponse = modelMapper.map(userDto, UserSignUpResponse.class);

        log.info(signUpResponse.toString());
        return ResponseEntity.ok(signUpResponse);
    }

    private SendNotificationDto createActivateSendEmailDto(UserDto user) {
        final Map<String, String> templateModel = new HashMap<>();

        templateModel.put("activationUrl", EmailUtil.createActivationUrl(user.getEmail(), "12345"));
        return SendNotificationDto
                .builder()
                .notificationTo(user.getEmail())
                .notificationType(NotificationType.EMAIL_ACTIVATION)
                .templateModel(templateModel)
                .build();
    }

    @PostMapping("/log-in")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid UserLogInRequest logInRequest) throws Exception {
        final UserCredentialsDto userCredentialsDto = modelMapper.map(logInRequest, UserCredentialsDto.class);
        final UserAndJwtDto userAndJwtDto = userService.authenticateUser(userCredentialsDto);

        if (!userAndJwtDto.isTwoFactorVerification()) {
            final UserLogInResponse logInResponse = modelMapper.map(userAndJwtDto, UserLogInResponse.class);

            log.info(logInRequest.toString());
            return ResponseEntity.ok(logInResponse);
        }

        final SendNotificationDto sendNotificationDto = buildVerificationCodeNotification(userAndJwtDto);
        try {
            notificationManager.queueNotification(sendNotificationDto);
        } catch (Exception exception) {
            log.error("notification error", exception);
        }

        return ResponseEntity.ok().build();
    }

    private SendNotificationDto buildVerificationCodeNotification(UserAndJwtDto userAndJwtDto) {
        final Map<String, String> templateModel = new HashMap<>();

        templateModel.put("code", userAndJwtDto.getVerificationCode());
        return SendNotificationDto
                .builder()
                .notificationTo(userAndJwtDto.getEmail())
                .notificationType(NotificationType.EMAIL_VERIFICATION)
                .templateModel(templateModel)
                .build();
    }

    @PostMapping("/log-in-verification-code")
    public ResponseEntity<?> createVerificationCodeAuthenticationToken(@RequestBody @Valid UserLogInVerificationCodeRequest userLogInVerificationCodeRequest) throws Exception {
        final UserCredentialsDto userCredentialsDto = modelMapper.map(userLogInVerificationCodeRequest, UserCredentialsDto.class);
        final UserAndJwtDto userAndJwtDto = userService.authenticateVerificationCodeUser(userCredentialsDto);
        final UserLogInResponse userLogInResponse = modelMapper.map(userAndJwtDto, UserLogInResponse.class);

        log.info(userLogInResponse.toString());
        return ResponseEntity.ok(userLogInResponse);
    }

}
