package com.lamar.primebox.notification.dto;

import com.lamar.primebox.notification.model.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class SendNotificationDto {

    @NotBlank
    private String notificationTo;

    @NotNull
    private NotificationType notificationType;

    @NotEmpty
    private Map<String, String> templateModel;

}
