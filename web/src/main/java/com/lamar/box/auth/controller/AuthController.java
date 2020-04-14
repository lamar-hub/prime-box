package com.lamar.box.auth.controller;

import com.lamar.box.auth.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lamar.box.auth.dto.UserDTO;
import com.lamar.box.auth.entity.User;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE, RequestMethod.PATCH })
public class AuthController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;

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
	public ResponseEntity<?> updateUser(@RequestHeader(name = "Authorization") String token) throws Exception{
		
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
