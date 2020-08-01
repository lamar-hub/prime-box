package com.lamar.primebox.email.service;

import com.lamar.primebox.email.dto.NotificationDto;
import com.lamar.primebox.email.dto.SendNotificationDto;

import java.util.List;

public interface NotificationService {

    void saveNotification(SendNotificationDto sendNotificationDto);
    
    List<NotificationDto> getNotificationChunk(Integer chunk);
    
    void removeNotification(NotificationDto notificationDto);
    
    void updateNotification(NotificationDto notificationDto);
    
}
