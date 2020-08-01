package com.lamar.primebox.email.dto;

import com.lamar.primebox.email.model.NotificationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class NotificationDto {

    private String notificationId;
    private String notificationTo;
    private NotificationType notificationType;
    private Integer attemptCount;
    private Map<String, String> templateModel;

}
