package com.lamar.box.sms.service;

import com.lamar.box.sms.model.SenderTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lamar.box.sms.config.TwilioConfiguration;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioService implements SmsService {
	
	@Autowired
	private TwilioConfiguration twilioConfiguration;

	@Override
	public void sendSMS(SenderTemplate senderTemplate) {
		PhoneNumber to = new PhoneNumber(senderTemplate.getPhoneNumber());
		PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
		String message = senderTemplate.getMessage();
		MessageCreator creator = new MessageCreator(to, from, message);
		creator.create();
	}

}
