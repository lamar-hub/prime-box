package com.lamar.primebox.email.sender.email;

import com.lamar.primebox.email.dto.NotificationDto;
import com.sendgrid.Response;

import java.io.IOException;

public interface EmailSender {

    Response sendEmailRequest(NotificationDto notificationDto) throws IOException;

}
