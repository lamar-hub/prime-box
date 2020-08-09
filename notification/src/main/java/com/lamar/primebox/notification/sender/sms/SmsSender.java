package com.lamar.primebox.notification.sender.sms;

import com.lamar.primebox.notification.dto.NotificationDto;

import java.io.IOException;

public interface SmsSender {

    String sendSms(NotificationDto notificationDto) throws IOException;
    
}
