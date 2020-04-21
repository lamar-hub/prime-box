package com.lamar.primebox.email.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridService implements EmailService {

    private final SendGrid sendGrid;

    public SendGridService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    @Override
    public void sendVerificationEmail(String to, String code) throws IOException {
        Email from = new Email("test@example.com");
        String subject = "Sending with SendGrid is Fun";
        Email toWho = new Email(to);
        Content content = new Content("text/plain", code);
        Mail mail = new Mail(from, subject, toWho, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sendGrid.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }

    @Override
    public void sendSharedFileEmail(String to, String fileID, String filename, String name, String surname, String email, String message) {

    }

    @Override
    public void sendAlertEmail(String to, String used) {

    }
}
