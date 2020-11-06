package com.lamar.primebox.notification.service;

import com.lamar.primebox.notification.dto.NotificationDto;
import com.lamar.primebox.notification.dto.NotificationWebhookDto;
import com.lamar.primebox.notification.dto.SendNotificationDto;
import com.lamar.primebox.notification.model.NotificationState;

import java.util.List;

public interface NotificationService {

    void saveNotification(SendNotificationDto sendNotificationDto);

    List<NotificationDto> getNotificationsByState(NotificationState notificationState);

    void updateNotification(NotificationDto notificationDto);

    void updateWebhookNotification(NotificationWebhookDto notificationWebhookDto);

}
