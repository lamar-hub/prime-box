package com.lamar.primebox.notification.dto;

import com.lamar.primebox.notification.model.NotificationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class NotificationWebhookDto {

    private NotificationState notificationState;
    private String transactionId;

}
