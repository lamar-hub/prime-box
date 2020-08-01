package com.lamar.primebox.email.manager;

import com.lamar.primebox.email.dto.SendNotificationDto;

public interface NotificationManager {

    void queueNotification(SendNotificationDto sendNotificationDto);

    void processNotification();

}
