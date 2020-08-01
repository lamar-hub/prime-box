package com.lamar.primebox.email.manager.impl;

import com.lamar.primebox.email.dto.NotificationDto;
import com.lamar.primebox.email.dto.SendNotificationDto;
import com.lamar.primebox.email.manager.NotificationManager;
import com.lamar.primebox.email.model.NotificationType;
import com.lamar.primebox.email.sender.email.EmailSender;
import com.lamar.primebox.email.sender.sms.SmsSender;
import com.lamar.primebox.email.service.NotificationService;
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
        final List<NotificationDto> emailChunk = notificationService.getNotificationChunk(20);

        emailChunk
                .parallelStream()
                .forEach(
                        notificationDto -> {
                            try {
                                if (notificationDto.getNotificationType() == NotificationType.SMS_MESSAGE) {
                                    smsSender.sendSms(notificationDto);
                                } else {
                                    emailSender.sendEmailRequest(notificationDto);
                                }
                            } catch (IOException ioException) {
                                log.error("Error sending notification: ", ioException);
                            }

                            completeNotification(notificationDto);
                        }
                );
    }

    private void completeNotification(NotificationDto notificationDto) {
        if (notificationDto.getAttemptCount() > 4) {
            notificationService.removeNotification(notificationDto);
        } else {
            notificationDto.setAttemptCount(notificationDto.getAttemptCount() + 1);
            notificationService.updateNotification(notificationDto);
        }
    }

}
