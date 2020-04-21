package com.lamar.primebox.sms.service;

import com.lamar.primebox.sms.model.SenderTemplate;

public interface SmsService {

    void sendSMS(SenderTemplate senderTemplate);

}
