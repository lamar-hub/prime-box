package com.lamar.drive.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lamar.drive.auth.entity.User;
import com.lamar.drive.auth.service.UserServiceImpl;
import com.lamar.drive.auth.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private UserServiceImpl jwtUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(
								HttpServletRequest request,
								HttpServletResponse response,
								FilterChain filterChain) throws ServletException, IOException {
		String tokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		System.out.println(tokenHeader);
		
		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			
			jwtToken = tokenHeader.substring(7);
			
			try {
				
				username = jwtUtil.getUsernameFromToken(jwtToken);
				
			} catch (Exception e) {
				System.out.println("Can not get username from token!");
			}
		} else {
			System.out.println("HTTP request doesn't contain Authorization header!");
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			User user = jwtUserDetailsService.loadUserByUsername(username);
			
			if(jwtUtil.validateToken(jwtToken, user)) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			
		}
		
		filterChain.doFilter(request, response);
	}
	
}
