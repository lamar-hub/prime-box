package com.lamar.primebox.sms.service;

import com.lamar.primebox.sms.config.TwilioConfiguration;
import com.lamar.primebox.sms.model.SenderTemplate;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioService implements SmsService {

    private final TwilioConfiguration twilioConfiguration;

    public TwilioService(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void sendSMS(SenderTemplate senderTemplate) {
        PhoneNumber to = new PhoneNumber(senderTemplate.getPhoneNumber());
        PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
        String message = senderTemplate.getMessage();
        MessageCreator creator = new MessageCreator(to, from, message);
        creator.create();
    }

}
