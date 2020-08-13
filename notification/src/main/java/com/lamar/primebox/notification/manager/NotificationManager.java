package com.lamar.primebox.notification.manager;

import com.lamar.primebox.notification.dto.NotificationWebhookDto;
import com.lamar.primebox.notification.dto.SendNotificationDto;

public interface NotificationManager {

    void queueNotification(SendNotificationDto sendNotificationDto);

    void processNotification();

    void submitNotification(NotificationWebhookDto notificationWebhookDto);

}
