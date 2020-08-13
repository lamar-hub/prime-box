package com.lamar.primebox.notification.controller;

import com.lamar.primebox.notification.dto.NotificationWebhookDto;
import com.lamar.primebox.notification.dto.request.SendGridWebhookRequest;
import com.lamar.primebox.notification.manager.NotificationManager;
import com.lamar.primebox.notification.model.NotificationState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.PATCH})
@RequestMapping("webhook/sendgrid")
@Slf4j
public class SendGridWebhookController {

    private final NotificationManager notificationManager;

    public SendGridWebhookController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @PostMapping
    public ResponseEntity<?> webhook(@RequestBody @Valid SendGridWebhookRequest sendGridWebhookRequest) {
        final NotificationWebhookDto notificationWebhookDto = mapToNotificationWebhookDto(sendGridWebhookRequest);

        try {
            notificationManager.submitNotification(notificationWebhookDto);
        } catch (Exception e) {
            log.error("sendgrid submit error", e);
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok().build();
    }

    private NotificationWebhookDto mapToNotificationWebhookDto(SendGridWebhookRequest sendGridWebhookRequest) {
        final String transactionId = sendGridWebhookRequest.getSgMessageId().split("\\.")[0];
        final NotificationState notificationState;

        switch (sendGridWebhookRequest.getEvent()) {
            case "processed":
                notificationState = NotificationState.PROCESSED;
                break;
            case "delivered":
                notificationState = NotificationState.DELIVERED;
                break;
            default:
                notificationState = NotificationState.ERROR;
        }

        return NotificationWebhookDto
                .builder()
                .notificationState(notificationState)
                .transactionId(transactionId)
                .build();
    }

}
