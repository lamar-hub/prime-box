package com.lamar.primebox.email.repo;

import com.lamar.primebox.email.model.Notification;

import java.util.List;

public interface NotificationDao {

    void saveNotification(Notification notification);

    List<Notification> getNotificationChunk(Integer chunk);

    void deleteNotification(Notification notification);

    void updateNotification(Notification notification);

}
