package com.lamar.primebox.web.controller;

import com.lamar.primebox.notification.dto.SendNotificationDto;
import com.lamar.primebox.notification.manager.NotificationManager;
import com.lamar.primebox.notification.model.NotificationType;
import com.lamar.primebox.web.dto.model.UserAndJwtDto;
import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.VerificationCodeDto;
import com.lamar.primebox.web.dto.request.UserLogInCodeRequest;
import com.lamar.primebox.web.dto.request.UserLogInRequest;
import com.lamar.primebox.web.dto.request.UserSignUpRequest;
import com.lamar.primebox.web.dto.response.UserSignUpResponse;
import com.lamar.primebox.web.service.UserService;
import com.lamar.primebox.web.service.VerificationCodeService;
import com.lamar.primebox.web.util.EmailUtil;
import com.lamar.primebox.web.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final VerificationCodeService verificationCodeService;
    private final AuthenticationManager authenticationManager;
    private final NotificationManager notificationManager;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService,
                          VerificationCodeService verificationCodeService,
                          AuthenticationManager authenticationManager,
                          NotificationManager notificationManager,
                          @Qualifier("webModelMapper") ModelMapper modelMapper,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.verificationCodeService = verificationCodeService;
        this.authenticationManager = authenticationManager;
        this.notificationManager = notificationManager;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponse> addUser(@RequestBody @Valid UserSignUpRequest signUpRequest) throws Exception {
        final UserBasicDto userBasicDto = modelMapper.map(signUpRequest, UserBasicDto.class);
        final UserDto userDto = userService.addUser(userBasicDto);

        final SendNotificationDto sendNotificationDto = buildActivateNotification(userDto);
        try {
            notificationManager.queueNotification(sendNotificationDto);
        } catch (Exception exception) {
            log.error("notification error", exception);
        }

        final UserSignUpResponse signUpResponse = modelMapper.map(userDto, UserSignUpResponse.class);

        log.info(signUpResponse.toString());
        return ResponseEntity.ok(signUpResponse);
    }

    private SendNotificationDto buildActivateNotification(UserDto userDto) {
        final Map<String, String> templateModel = new HashMap<>();

        templateModel.put("activationUrl",
                          EmailUtil.createActivationUrl(userDto.getEmail(), userDto.getUserId()));
        log.info(templateModel.get("activationUrl"));
        return SendNotificationDto
                .builder()
                .notificationTo(userDto.getEmail())
                .notificationType(NotificationType.EMAIL_ACTIVATION)
                .templateModel(templateModel)
                .build();
    }

    @PostMapping("/log-in")
    public ResponseEntity<?> logIn(@RequestBody @Valid UserLogInRequest logInRequest) throws Exception {
        final UserDto userDto = userService.getUser(logInRequest.getEmail());

        authenticate(userDto.getEmail(), logInRequest.getPassword());

        if (userDto.isTwoFactorVerification()) {
            final VerificationCodeDto verificationCodeDto = verificationCodeService.createVerificationCode(userDto.getEmail());

            final SendNotificationDto sendNotificationDto = buildVerificationCodeNotification(userDto.getEmail(), verificationCodeDto.getCode());
            try {
                notificationManager.queueNotification(sendNotificationDto);
            } catch (Exception exception) {
                log.error("notification error", exception);
            }

            return ResponseEntity.ok().build();
        }

        final UserAndJwtDto userAndJwtDto = modelMapper.map(userDto, UserAndJwtDto.class);
        final String jwtToken = jwtUtil.generateToken(userDto.getUserId(), userDto.getEmail());
        userAndJwtDto.setJwtToken(jwtToken);
        return ResponseEntity.ok(userAndJwtDto);
    }

    @PostMapping("/log-in-code")
    public ResponseEntity<?> logIn(@RequestBody @Valid UserLogInCodeRequest userLogInCodeRequest) throws Exception {
        final UserDto userDto = userService.getUser(userLogInCodeRequest.getEmail());

        authenticate(userLogInCodeRequest.getEmail(), userLogInCodeRequest.getPassword());

        if (!verificationCodeService.checkVerificationCode(userLogInCodeRequest.getEmail(), userLogInCodeRequest.getVerificationCode())) {
            log.error("code not valid");
            throw new Exception("code not valid");
        }

        final UserAndJwtDto userAndJwtDto = modelMapper.map(userDto, UserAndJwtDto.class);
        final String jwtToken = jwtUtil.generateToken(userDto.getUserId(), userDto.getEmail());
        userAndJwtDto.setJwtToken(jwtToken);
        return ResponseEntity.ok(userAndJwtDto);
    }

    private void authenticate(String username, String password) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    private SendNotificationDto buildVerificationCodeNotification(String email, String code) {
        final Map<String, String> templateModel = new HashMap<>();

        templateModel.put("code", code);
        return SendNotificationDto
                .builder()
                .notificationTo(email)
                .notificationType(NotificationType.EMAIL_VERIFICATION)
                .templateModel(templateModel)
                .build();
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestParam String username, @RequestParam String encodedSecret) throws Exception {
        final UserDto userDto = userService.getUser(username);

        if (userDto.isActive()) {
            throw new Exception("user already active");
        }

        final String secretHmacSha256Encoded = EmailUtil.createSecretHmacSha256Encoded(userDto.getEmail(), userDto.getUserId());

        if (!encodedSecret.equals(secretHmacSha256Encoded)) {
            throw new Exception("wrong secret");
        }

        userService.activateUser(userDto.getEmail());
        return ResponseEntity.ok().build();
    }

}
