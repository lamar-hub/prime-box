package com.lamar.primebox.notification.sender.email;

import com.lamar.primebox.notification.dto.NotificationDto;

import java.io.IOException;

public interface EmailSender {

    String sendEmailRequest(NotificationDto notificationDto) throws IOException;

}
