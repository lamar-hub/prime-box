package com.lamar.primebox.notification.manager.impl;

import com.lamar.primebox.notification.dto.NotificationDto;
import com.lamar.primebox.notification.dto.NotificationWebhookDto;
import com.lamar.primebox.notification.dto.SendNotificationDto;
import com.lamar.primebox.notification.manager.NotificationManager;
import com.lamar.primebox.notification.model.NotificationState;
import com.lamar.primebox.notification.model.NotificationType;
import com.lamar.primebox.notification.sender.email.EmailSender;
import com.lamar.primebox.notification.sender.sms.SmsSender;
import com.lamar.primebox.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationManagerImpl implements NotificationManager {

    private final Validator validator;
    private final NotificationService notificationService;
    private final EmailSender emailSender;
    private final SmsSender smsSender;

    public NotificationManagerImpl(Validator validator, NotificationService notificationService, EmailSender emailSender, SmsSender smsSender) {
        this.validator = validator;
        this.notificationService = notificationService;
        this.emailSender = emailSender;
        this.smsSender = smsSender;
    }


    @Override
    public void queueNotification(SendNotificationDto sendNotificationDto) {
        final Set<ConstraintViolation<SendNotificationDto>> violations = validator.validate(sendNotificationDto);
        if (!violations.isEmpty()) {
            final String validationErrors = String.join(
                    ", ",
                    violations
                            .stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.toSet())
            );

            throw new ValidationException(validationErrors);
        }

        notificationService.saveNotification(sendNotificationDto);
    }

    @Scheduled(fixedRate = 10000)
    @Override
    public void processNotification() {
        final List<NotificationDto> notificationChunk = notificationService.getNotificationChunk(20);

        notificationChunk
                .parallelStream()
                .forEach(
                        notificationDto -> {
                            try {
                                final String notificationTransactionId;

                                if (notificationDto.getNotificationType() == NotificationType.SMS_MESSAGE) {
                                    notificationTransactionId = smsSender.sendSms(notificationDto);
                                } else {
                                    notificationTransactionId = emailSender.sendEmailRequest(notificationDto);
                                }

                                if (notificationTransactionId != null) {
                                    notificationDto.setNotificationState(NotificationState.PENDING);
                                } else {
                                    throw new IOException("notification transaction id is null");
                                }

                            } catch (IOException ioException) {
                                log.error("error sending notification: ", ioException);
                                notificationDto.setNotificationState(NotificationState.ERROR);
                            }

                            notificationService.updateNotification(notificationDto);
                        }
                );
    }

    @Override
    public void submitNotification(NotificationWebhookDto notificationWebhookDto) {
        notificationService.updateWebhookNotification(notificationWebhookDto);
    }

}
