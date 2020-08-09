package com.lamar.primebox.notification.service;

import com.lamar.primebox.notification.dto.NotificationDto;
import com.lamar.primebox.notification.dto.SendNotificationDto;

import java.util.List;

public interface NotificationService {

    void saveNotification(SendNotificationDto sendNotificationDto);
    
    List<NotificationDto> getNotificationChunk(Integer chunk);

    NotificationDto getNotificationByTransactionId(String transactionId);
    
    void updateNotification(NotificationDto notificationDto);
    
}
