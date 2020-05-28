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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH})
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PatchMapping("")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest updateRequest) throws Exception {
        UserBasicDto userBasicDto = modelMapper.map(updateRequest, UserBasicDto.class);
        userBasicDto.setUsername(getUsernameFromSecurityContext());
        UserDto userDto = this.userService.updateUser(userBasicDto);
        UserUpdateResponse updateResponse = modelMapper.map(userDto, UserUpdateResponse.class);
        return ResponseEntity.ok(updateResponse);
    }

    @DeleteMapping("")
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
