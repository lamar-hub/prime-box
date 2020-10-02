package com.lamar.primebox.notification.sender.email.impl;

import com.lamar.primebox.notification.constant.PrimeBoxEmailConstants;
import com.lamar.primebox.notification.dto.NotificationDto;
import com.lamar.primebox.notification.sender.email.EmailSender;
import com.lamar.primebox.notification.sender.email.TemplateResolver;
import com.lamar.primebox.notification.sender.email.model.Mail;
import com.lamar.primebox.notification.sender.email.model.objects.Content;
import com.lamar.primebox.notification.sender.email.model.objects.Email;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
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
    public String sendEmailRequest(NotificationDto notificationDto) throws IOException {
        final Mail mail = buildMail(notificationDto);
        final Request request = buildRequest(mail);
        final Response response = sendGrid.api(request);

        if (response.getStatusCode() != 202) {
            throw new IOException("status code is not 202 Accepted");
        }

        return response.getHeaders().get("X-Message-Id");
    }

    private Request buildRequest(Mail mail) throws IOException {
        final Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint(PrimeBoxEmailConstants.ENDPOINT);
        request.setBody(mail.build());
        return request;
    }

    private Mail buildMail(NotificationDto notificationDto) {
        final Email from = new Email(PrimeBoxEmailConstants.EMAIL_FROM, PrimeBoxEmailConstants.EMAIL_FROM_NAME);
        final String subject = notificationDto.getNotificationType().getSubject();
        final Email to = new Email(notificationDto.getNotificationTo());
        final Map<String, Object> templateModel = new HashMap<>(notificationDto.getTemplateModel());
        final String htmlContent =
                templateResolver.resolveHtmlTemplateToString(notificationDto.getNotificationType().getView(), templateModel);
        final Content content = new Content(ContentType.TEXT_HTML.getMimeType(), htmlContent);

        return new Mail(from, subject, to, content);
    }

}
