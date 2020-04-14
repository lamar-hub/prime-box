package com.lamar.box.auth.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lamar.box.auth.dao.UserDAO;
import com.lamar.box.auth.entity.User;
import com.lamar.box.auth.util.JwtUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public void validateEmail(User user) throws Exception {

		HttpResponse<JsonNode> response = Unirest
				.get("https://email-checker.p.rapidapi.com/verify/v1?email=" + user.getEmail())
				.header("x-rapidapi-host", "email-checker.p.rapidapi.com")
				.header("x-rapidapi-key", "0fbec8d08emsh0449a8663ed4a14p1e6575jsn438dd4059cbc").asJson();

		if (response.getBody().getObject().get("status").equals("invalid")) {
			throw new Exception("Email is not valid!");
		}
	}

	@Override
	@Transactional
	public User loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userDAO.getByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("USER_NOT_FOUND '" + username + "'");
		}

		return user;
	}

	@Override
	@Transactional
	public void addUser(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setStored(0l);
		user.setLimit(500000000l);

		user = userDAO.saveUser(user);
	}

	@Override
	public void authenticate(User user) throws Exception {
		try {

			UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user.getUsername(),
					user.getPassword());

			authenticationManager.authenticate(upat);

		} catch (DisabledException e) {
			throw new Exception("User disabled exception!", e);
		} catch (Exception e) {
			throw new Exception("Invalid credentials", e);
		}
	}

	@Override
	public void generateToken(User user) {

		user.setToken(jwtUtil.generateToken(user));
		user.setExpireIn(1000 * 60 * 60);

	}

	@Override
	@Transactional
	public User updateUserPlan(String userID) throws Exception {

		User user = userDAO.getUser(userID);

		if (user.getLimit() == 500000000l) {
			user.setLimit(5000000000l);
		} else {
			if (user.getLimit() == 5000000000l) {
				user.setLimit(100000000000l);
			} else {
				throw new Exception("Invalid uprading");
			}
		}
		
		return user;
	}

	@Override
	@Transactional
	public void deleteUser(String userID) throws Exception {

		int deleted = userDAO.deleteUser(userID);

		if (deleted != 1) {
			throw new Exception("Invalid deleting");
		}

	}

	@Override
	public String getUserIDFromToken(String token) {
		
		return this.jwtUtil.getUserIDFromToken(token.split(" ")[1]);
	}

}
