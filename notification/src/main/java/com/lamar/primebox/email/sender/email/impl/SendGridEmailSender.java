package com.lamar.primebox.email.sender.email.impl;

import com.lamar.primebox.email.constant.PrimeBoxEmailConstants;
import com.lamar.primebox.email.dto.NotificationDto;
import com.lamar.primebox.email.sender.email.EmailSender;
import com.lamar.primebox.email.sender.email.TemplateResolver;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SendGridEmailSender implements EmailSender {

    private final SendGrid sendGrid;
    private final TemplateResolver templateResolver;

    public SendGridEmailSender(SendGrid sendGrid, TemplateResolver templateResolver) {
        this.sendGrid = sendGrid;
        this.templateResolver = templateResolver;
    }

    @Override
    public Response sendEmailRequest(NotificationDto notificationDto) throws IOException {
        final Mail mail = buildMail(notificationDto);
        final Request request = buildRequest(mail);

        return sendGrid.api(request);
    }

    private Request buildRequest(Mail mail) throws IOException {
        final Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint(PrimeBoxEmailConstants.ENDPOINT);
        request.setBody(mail.build());
        return request;
    }

    private Mail buildMail(NotificationDto notificationDto) {
        final Email from = new Email(PrimeBoxEmailConstants.EMAIL_FROM);
        final String subject = notificationDto.getNotificationType().getSubject();
        final Email to = new Email(notificationDto.getNotificationTo());
        final Map<String, Object> templateModel = new HashMap<>(notificationDto.getTemplateModel());
        final String htmlContent =
                templateResolver.resolveHtmlTemplateToString(notificationDto.getNotificationType().getView(), templateModel);
        final Content content = new Content(ContentType.TEXT_HTML.getMimeType(), htmlContent);

        return new Mail(from, subject, to, content);
    }

}
