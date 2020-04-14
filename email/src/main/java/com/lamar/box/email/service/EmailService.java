package com.lamar.box.email.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {

	@Async
	public void sendVerificationEmail(String to, String code) throws UnsupportedEncodingException, MessagingException;
	
	@Async
	public void sendSharedFileEmail(String to, String fileID, String filename, String name, String surname, String email, String message) throws UnsupportedEncodingException, MessagingException;

	@Async
	public void sendAlertEmail(String to, String used) throws UnsupportedEncodingException, MessagingException;
}
