package com.lamar.primebox.web.controller;

import com.lamar.primebox.web.dto.model.UserBasicDto;
import com.lamar.primebox.web.dto.model.UserCredentialsDto;
import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserUpdateDto;
import com.lamar.primebox.web.dto.request.UserUpdateRequest;
import com.lamar.primebox.web.dto.response.UserDeactivateResponse;
import com.lamar.primebox.web.dto.response.UserUpdateResponse;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.model.UserCredentials;
import com.lamar.primebox.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequest updateRequest) throws Exception {
        final UserUpdateDto userUpdateDto = modelMapper.map(updateRequest, UserUpdateDto.class)
                .setUsername(getUsernameFromSecurityContext());
        final UserDto userDto = this.userService.updateUser(userUpdateDto);
        final UserUpdateResponse updateResponse = modelMapper.map(userDto, UserUpdateResponse.class);

        log.info(updateResponse.toString());
        return ResponseEntity.ok(updateResponse);
    }

    @DeleteMapping
    public ResponseEntity<?> deactivateUser() throws Exception {
        final UserCredentialsDto userCredentialsDto = userService.deactivateUser(getUsernameFromSecurityContext());
        final UserDeactivateResponse userDeactivateResponse = modelMapper.map(userCredentialsDto, UserDeactivateResponse.class);

        log.info(userDeactivateResponse.toString());
        return ResponseEntity.ok(userDeactivateResponse);
    }

    private String getUsernameFromSecurityContext() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserCredentials userCredentials = (UserCredentials) authentication.getPrincipal();

        return userCredentials.getUsername();
    }

}
