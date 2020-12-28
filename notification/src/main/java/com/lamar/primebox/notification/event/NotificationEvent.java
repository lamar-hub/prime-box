package com.lamar.primebox.notification.event;

import com.lamar.primebox.notification.dto.SendNotificationDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
public class NotificationEvent extends ApplicationEvent {

    private final SendNotificationDto notificationDto;
    
    public NotificationEvent(Object source, SendNotificationDto notificationDto) {
        super(source);
        this.notificationDto = notificationDto;
    }
    
}
