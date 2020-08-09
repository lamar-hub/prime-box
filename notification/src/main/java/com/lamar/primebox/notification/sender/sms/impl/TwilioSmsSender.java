package com.lamar.primebox.notification.sender.sms.impl;

import com.lamar.primebox.notification.dto.NotificationDto;
import com.lamar.primebox.notification.sender.sms.SmsSender;
import com.twilio.exception.ApiException;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class TwilioSmsSender implements SmsSender {

    @Value("${twilio.trial_number}")
    private String twilioSenderNumber;

    private final TwilioRestClient twilioRestClient;

    public TwilioSmsSender(TwilioRestClient twilioRestClient) {
        this.twilioRestClient = twilioRestClient;
    }

    @Override
    public String sendSms(NotificationDto notificationDto) throws IOException {
        final PhoneNumber to = new PhoneNumber(notificationDto.getNotificationTo());
        final PhoneNumber from = new PhoneNumber(twilioSenderNumber);
        final String body = notificationDto.getTemplateModel().getOrDefault("message", "Message");
        final MessageCreator messageCreator = Message.creator(to, from, body);

        try {
            final Message message = messageCreator.create(twilioRestClient);

            log.info(message.toString());
            return message.getSid();
        } catch (ApiException apiException) {
            log.error(apiException.toString());
            throw new IOException(apiException.getMessage());
        }
    }

}
