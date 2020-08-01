package com.lamar.primebox.email.sender.sms;

import com.lamar.primebox.email.dto.NotificationDto;
import com.twilio.rest.api.v2010.account.Message;

import java.io.IOException;

public interface SmsSender {

    Message.Status sendSms(NotificationDto notificationDto) throws IOException;
    
}
