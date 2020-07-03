package com.lamar.primebox.web.controller;

import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserJwtDto;
import com.lamar.primebox.web.dto.request.UserLogInRequest;
import com.lamar.primebox.web.dto.request.UserSignUpRequest;
import com.lamar.primebox.web.dto.response.UserLogInResponse;
import com.lamar.primebox.web.dto.response.UserSignUpResponse;
import com.lamar.primebox.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH})
@Slf4j
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public AuthController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponse> addUser(@RequestBody @Valid UserSignUpRequest signUpRequest) throws Exception {
        final UserBasicDto userBasicDto = modelMapper.map(signUpRequest, UserBasicDto.class);
        final UserDto userDto = userService.addUser(userBasicDto);
        final UserSignUpResponse signUpResponse = modelMapper.map(userDto, UserSignUpResponse.class);
        return ResponseEntity.ok(signUpResponse);
    }

    @PostMapping("/log-in")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid UserLogInRequest logInRequest) throws Exception {
        final UserCredentialsDto userCredentialsDto = modelMapper.map(logInRequest, UserCredentialsDto.class);
        final UserJwtDto userJwtDto = userService.authenticateUser(userCredentialsDto);
        final UserLogInResponse logInResponse = modelMapper.map(userJwtDto, UserLogInResponse.class);
        return ResponseEntity.ok(logInResponse);
    }

}
