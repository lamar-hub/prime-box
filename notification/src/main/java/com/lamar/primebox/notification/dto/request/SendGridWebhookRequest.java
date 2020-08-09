package com.lamar.primebox.notification.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SendGridWebhookRequest {

    private String event;
    private String sgMessageId;

}
