package com.lamar.primebox.notification;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource(name = "default", value = "classpath:notification.properties"),
})
public class NotificationApplication {
}
