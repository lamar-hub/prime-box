package com.lamar.primebox.notification.dto;

import com.lamar.primebox.notification.model.NotificationState;
import com.lamar.primebox.notification.model.NotificationType;
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
    private String notificationTransactionId;
    private String notificationTo;
    private NotificationType notificationType;
    private NotificationState notificationState;
    private Integer attemptCount;
    private Map<String, String> templateModel;

}
