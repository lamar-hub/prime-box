package com.lamar.primebox.notification.repo;

import com.lamar.primebox.notification.model.Notification;
import com.lamar.primebox.notification.model.NotificationState;

import java.util.List;

public interface NotificationDao {

    void saveNotification(Notification notification);

    List<Notification> getNotificationsByState(NotificationState notificationState);

    Notification getNotificationByTransactionId(String transactionId);

    void updateNotification(Notification notification);

}
