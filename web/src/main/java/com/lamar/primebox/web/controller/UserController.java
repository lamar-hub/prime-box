package com.lamar.primebox.web.controller;

import com.lamar.primebox.web.dto.model.UserDto;
import com.lamar.primebox.web.dto.model.UserUpdateDto;
import com.lamar.primebox.web.dto.request.UserUpdateRequest;
import com.lamar.primebox.web.dto.response.UserDeactivateResponse;
import com.lamar.primebox.web.dto.response.UserUpdateResponse;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public UserController(UserService userService, @Qualifier("webModelMapper") ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PutMapping
    public ResponseEntity<?> updatePlanUser() throws Exception {
        final UserDto userDto = this.userService.updateUserPlan(getUsernameFromSecurityContext());
        final UserUpdateResponse updateResponse = modelMapper.map(userDto, UserUpdateResponse.class);

        log.info(updateResponse.toString());
        return ResponseEntity.ok(updateResponse);
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

    @GetMapping
    public ResponseEntity<?> activateUser() throws Exception {
        final UserDto userDto = userService.activateUser(getUsernameFromSecurityContext());

        log.info(userDto.toString());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deactivateUser() throws Exception {
        final UserDto userDto = userService.deactivateUser(getUsernameFromSecurityContext());
        final UserDeactivateResponse userDeactivateResponse = modelMapper.map(userDto, UserDeactivateResponse.class);

        log.info(userDeactivateResponse.toString());
        return ResponseEntity.ok(userDeactivateResponse);
    }

    private String getUsernameFromSecurityContext() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((User) authentication.getPrincipal()).getEmail();
    }

}
