package com.lamar.primebox.web.controller;

import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserJwtDto;
import com.lamar.primebox.web.dto.request.UserLogInRequest;
import com.lamar.primebox.web.dto.request.UserSignUpRequest;
import com.lamar.primebox.web.dto.request.UserUpdateRequest;
import com.lamar.primebox.web.dto.response.UserDeleteResponse;
import com.lamar.primebox.web.dto.response.UserLogInResponse;
import com.lamar.primebox.web.dto.response.UserSignUpResponse;
import com.lamar.primebox.web.dto.response.UserUpdateResponse;
import com.lamar.primebox.web.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH})
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponse> addUser(@RequestBody UserSignUpRequest signUpRequest) throws Exception {
        UserBasicDto userBasicDto = modelMapper.map(signUpRequest, UserBasicDto.class);
        UserDto userDto = userService.addUser(userBasicDto);
        UserSignUpResponse signUpResponse = modelMapper.map(userDto, UserSignUpResponse.class);
        return ResponseEntity.ok(signUpResponse);
    }

    @PostMapping("/log-in")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLogInRequest logInRequest) throws Exception {
        UserCredentialsDto userCredentialsDto = modelMapper.map(logInRequest, UserCredentialsDto.class);
        UserJwtDto userJwtDto = userService.authenticateUser(userCredentialsDto);
        UserLogInResponse logInResponse = modelMapper.map(userJwtDto, UserLogInResponse.class);
        return ResponseEntity.ok(logInResponse);
    }

    @PatchMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest updateRequest) throws Exception {
        UserBasicDto userBasicDto = modelMapper.map(updateRequest, UserBasicDto.class);
        userBasicDto.setUsername(getUsernameFromSecurityContext());
        UserDto userDto = this.userService.updateUser(userBasicDto);
        UserUpdateResponse updateResponse = modelMapper.map(userDto, UserUpdateResponse.class);
        return ResponseEntity.ok(updateResponse);
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deactivateUser() throws Exception {
        UserDto userDto = userService.deactivateUser(getUsernameFromSecurityContext());
        UserDeleteResponse userDeleteResponse = modelMapper.map(userDto, UserDeleteResponse.class);
        return ResponseEntity.ok(userDeleteResponse);
    }

    private String getUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

}
