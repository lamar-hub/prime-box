package com.lamar.box.email.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class GmailService implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	TemplateEngine templateEngine;
	
	@Async
	@Override
	public void sendVerificationEmail(String to, String code) throws UnsupportedEncodingException, MessagingException {
		
		Context context = new Context();
		context.setVariable("code", code);
		
		MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		message.setFrom("lazarmarinkovic29@gmail.com", "PRIME BOX");
		message.setTo(to);
		message.setSubject("PRIME BOX verification code");
		
		String htmlContent = this.templateEngine.process("verification-email", context);
		System.out.println(htmlContent);
		message.setText(htmlContent, true);
		
		this.mailSender.send(mimeMessage);
	}
	
	@Async
	@Override
	public void sendSharedFileEmail(String to, String fileID, String filename, String name, String surname, String email, String message) throws UnsupportedEncodingException, MessagingException {
		
		Context context = new Context();
		context.setVariable("fileID", fileID);
		context.setVariable("filename", filename);
		context.setVariable("name", name);
		context.setVariable("surname", surname);
		context.setVariable("email", email);
		context.setVariable("message", message);
		
		MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		messageHelper.setFrom("lazarmarinkovic29@gmail.com", "PRIME BOX");
		messageHelper.setTo(to);
		messageHelper.setSubject("PRIME BOX sharing alert");
		
		String htmlContent = this.templateEngine.process("shared-file-email", context);
		System.out.println(htmlContent);
		messageHelper.setText(htmlContent, true);
		
		this.mailSender.send(mimeMessage);
	}
	
	@Async
	@Override
	public void sendAlertEmail(String to, String used) throws UnsupportedEncodingException, MessagingException {
		
		Context context = new Context();
		context.setVariable("used", used);

		
		MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		messageHelper.setFrom("lazarmarinkovic29@gmail.com", "PRIME BOX");
		messageHelper.setTo(to);
		messageHelper.setSubject("PRIME BOX space usage alert");
		
		String htmlContent = this.templateEngine.process("alert-email", context);
		System.out.println(htmlContent);
		messageHelper.setText(htmlContent, true);
		
		this.mailSender.send(mimeMessage);
	}

}
