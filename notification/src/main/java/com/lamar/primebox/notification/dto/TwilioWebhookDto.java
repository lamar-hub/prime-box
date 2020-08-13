package com.lamar.primebox.notification.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TwilioWebhookDto {

    private String messageStatus;
    private String messageSid;

}
