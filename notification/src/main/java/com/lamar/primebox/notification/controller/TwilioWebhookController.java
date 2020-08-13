package com.lamar.primebox.notification.controller;

import com.lamar.primebox.notification.dto.NotificationWebhookDto;
import com.lamar.primebox.notification.dto.request.TwilioWebhookRequest;
import com.lamar.primebox.notification.manager.NotificationManager;
import com.lamar.primebox.notification.model.NotificationState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH})
@RequestMapping("webhook/twilio")
@Slf4j
public class TwilioWebhookController {

    private final NotificationManager notificationManager;

    public TwilioWebhookController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @PostMapping
    public ResponseEntity<?> webhook(@RequestBody @Valid TwilioWebhookRequest twilioWebhookRequest) {
        final NotificationWebhookDto notificationWebhookDto = mapToNotificationWebhookDto(twilioWebhookRequest);

        try {
            notificationManager.submitNotification(notificationWebhookDto);
        } catch (Exception e) {
            log.error("sendgrid submit error", e);
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok().build();
    }

    private NotificationWebhookDto mapToNotificationWebhookDto(TwilioWebhookRequest twilioWebhookRequest) {
        final NotificationState notificationState;

        switch (twilioWebhookRequest.getMessageStatus()) {
            case "queued":
                notificationState = NotificationState.PROCESSED;
                break;
            case "sent":
            case "delivered":
                notificationState = NotificationState.DELIVERED;
                break;
            default:
                notificationState = NotificationState.ERROR;
        }

        return NotificationWebhookDto
                .builder()
                .notificationState(notificationState)
                .transactionId(twilioWebhookRequest.getMessageSid())
                .build();
    }

}
