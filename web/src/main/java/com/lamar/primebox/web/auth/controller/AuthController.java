package com.lamar.primebox.web.auth.controller;

import com.lamar.primebox.web.auth.dto.UserDTO;
import com.lamar.primebox.web.auth.entity.User;
import com.lamar.primebox.web.auth.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH})
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public AuthController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) throws Exception {
        User user = modelMapper.map(userDTO, User.class);
        userService.validateEmail(user);
        userService.addUser(user);
        userService.generateToken(user);
        userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody UserDTO userDTO) throws Exception {
        User user = modelMapper.map(userDTO, User.class);
        this.userService.authenticate(user);
        user = (User) userService.loadUserByUsername(user.getUsername());
        userService.generateToken(user);
        userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/users")
    public ResponseEntity<?> updateUser(@RequestHeader(name = "Authorization") String token) throws Exception {
        String userID = this.userService.getUserIDFromToken(token);
        User user = this.userService.updateUserPlan(userID);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestHeader(name = "Authorization") String token) throws Exception {
        String userID = this.userService.getUserIDFromToken(token);
        userService.deleteUser(userID);
        return ResponseEntity.ok().build();
    }

}
