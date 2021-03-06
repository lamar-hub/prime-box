package com.lamar.primebox.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendGridWebhookRequest {

    @NotEmpty
    private String event;
    @NotEmpty
    private String sgMessageId;

}
