package com.lamar.primebox.notification.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SendGridWebhookDto {

    private String event;
    private String sgMessageId;

}
