package com.lamar.primebox.notification.repo;

import com.lamar.primebox.notification.model.Notification;

import java.util.List;

public interface NotificationDao {

    void saveNotification(Notification notification);

    List<Notification> getNotificationChunk(Integer chunk);

    Notification getNotificationByTransactionId(String transactionId);

    void updateNotification(Notification notification);

}
