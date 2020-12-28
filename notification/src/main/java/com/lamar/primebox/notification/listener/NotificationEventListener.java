package com.lamar.primebox.notification.listener;

import com.lamar.primebox.notification.event.NotificationEvent;
import com.lamar.primebox.notification.manager.NotificationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "notification.event.listener", value = "enabled")
public class NotificationEventListener implements ApplicationListener<NotificationEvent> {

    private final NotificationManager notificationManager;

    public NotificationEventListener(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @Override
    public void onApplicationEvent(NotificationEvent notificationEvent) {
        try {
            log.info("received notification: " + notificationEvent.getNotificationDto());
            notificationManager.queueNotification(notificationEvent.getNotificationDto());
        } catch (Exception e) {
            log.error("unable to queue notification", e);
        }
    }
    
}
