package com.lamar.primebox.email.dto;

import com.lamar.primebox.email.model.NotificationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SendNotificationDto {

    @NotBlank
    private String notificationTo;

    @NotNull
    private NotificationType notificationType;

    @NotEmpty
    private Map<String, String> templateModel;

}
