package com.lamar.primebox.email.service;

import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    @Async
    void sendVerificationEmail(String to, String code) throws IOException, MessagingException;

    @Async
    void sendSharedFileEmail(String to, String fileID, String filename, String name, String surname, String email, String message) throws UnsupportedEncodingException, MessagingException;

    @Async
    void sendAlertEmail(String to, String used) throws UnsupportedEncodingException, MessagingException;
}
