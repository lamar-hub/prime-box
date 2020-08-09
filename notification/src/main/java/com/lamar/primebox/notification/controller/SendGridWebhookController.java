package com.lamar.primebox.notification.controller;

import com.lamar.primebox.notification.dto.SendGridWebhookDto;
import com.lamar.primebox.notification.dto.request.SendGridWebhookRequest;
import com.lamar.primebox.notification.manager.NotificationManager;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public SendGridWebhookController(NotificationManager notificationManager, ModelMapper modelMapper) {
        this.notificationManager = notificationManager;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<?> webhook(@RequestBody @Valid SendGridWebhookRequest sendGridWebhookRequest) {
        log.info("sendGridWebhookRequest: " + sendGridWebhookRequest);

        final SendGridWebhookDto sendGridWebhookDto = modelMapper.map(sendGridWebhookRequest, SendGridWebhookDto.class);

        try {
            notificationManager.submitNotification(sendGridWebhookDto);
        } catch (Exception e) {
            log.error("submit error", e);
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok().build();
    }

}
