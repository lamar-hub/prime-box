package com.lamar.primebox.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwilioWebhookRequest {

    @NotBlank
    private String messageStatus;
    @NotBlank
    private String messageSid;

}
