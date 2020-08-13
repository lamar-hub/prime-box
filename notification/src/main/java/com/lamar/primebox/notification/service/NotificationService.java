package com.lamar.primebox.notification.service;

import com.lamar.primebox.notification.dto.NotificationDto;
import com.lamar.primebox.notification.dto.NotificationWebhookDto;
import com.lamar.primebox.notification.dto.SendNotificationDto;

import java.util.List;

public interface NotificationService {

    void saveNotification(SendNotificationDto sendNotificationDto);

    List<NotificationDto> getNotificationChunk(Integer chunk);

    void updateNotification(NotificationDto notificationDto);

    void updateWebhookNotification(NotificationWebhookDto notificationWebhookDto);

}
