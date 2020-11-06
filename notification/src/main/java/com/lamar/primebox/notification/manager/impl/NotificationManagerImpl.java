package com.lamar.primebox.notification.manager.impl;

import com.lamar.primebox.notification.dto.NotificationDto;
import com.lamar.primebox.notification.dto.NotificationWebhookDto;
import com.lamar.primebox.notification.dto.SendNotificationDto;
import com.lamar.primebox.notification.manager.NotificationManager;
import com.lamar.primebox.notification.model.NotificationState;
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
import java.util.Date;
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
    public void processInitNotifications() {
        log.info("processInitNotification");

        final List<NotificationDto> initNotifications = notificationService.getNotificationsByState(NotificationState.INIT);
        log.info("init notifications: " + initNotifications);

        initNotifications
                .parallelStream()
                .forEach(
                        notificationDto -> {
                            try {
                                final String transactionId = processNotification(notificationDto);

                                notificationDto.setNotificationTransactionId(transactionId);
                            } catch (IOException ioException) {
                                log.error("error sending notification: ", ioException);
                                notificationDto.setNotificationState(NotificationState.RESEND);
                            }

                            notificationDto.setMtime(new Date().getTime());
                            notificationService.updateNotification(notificationDto);
                        }
                        );
    }

    @Scheduled(fixedRate = 30000)
    @Override
    public void processResendNotifications() {
        log.info("processResendNotification");

        final List<NotificationDto> resendNotifications = notificationService.getNotificationsByState(NotificationState.RESEND);
        log.info("resend notifications: " + resendNotifications);
        
        resendNotifications
                .parallelStream()
                .forEach(
                        notificationDto -> {
                            try {
                                final String transactionId = processNotification(notificationDto);

                                notificationDto.setNotificationTransactionId(transactionId);
                            } catch (IOException ioException) {
                                log.error("error sending notification: ", ioException);
                                notificationDto.setNotificationState(NotificationState.ERROR);
                            }

                            notificationDto.setMtime(new Date().getTime());
                            notificationService.updateNotification(notificationDto);
                        }
                        );
    }


    @Scheduled(fixedRate = 60000)
    @Override
    public void checkPendingNotifications() {
        log.info("checkPendingNotifications");

        final List<NotificationDto> pendingNotifications = notificationService.getNotificationsByState(NotificationState.PENDING);
        log.info("pending notifications: " + pendingNotifications);

        pendingNotifications
                .parallelStream()
                .forEach(
                        notificationDto -> {
                            final Date currentDate = new Date();

                            if (currentDate.getTime() - notificationDto.getMtime() - 1000 * 60 * 60 * 10 < 0) {
                                return;
                            }

                            notificationDto.setNotificationState(NotificationState.ERROR);
                            notificationDto.setMtime(currentDate.getTime());
                            notificationService.updateNotification(notificationDto);
                        }
                        );
    }

    @Override
    public void submitNotification(NotificationWebhookDto notificationWebhookDto) {
        notificationService.updateWebhookNotification(notificationWebhookDto);
    }

    private String processNotification(NotificationDto notificationDto) throws IOException {
        final String notificationTransactionId;

        switch (notificationDto.getNotificationType()) {
            case EMAIL_ALERT:
            case EMAIL_ACTIVATION:
            case EMAIL_SHARED_FILE:
            case EMAIL_VERIFICATION:
                notificationTransactionId = emailSender.sendEmailRequest(notificationDto);
                break;
            case SMS_MESSAGE:
                notificationTransactionId = smsSender.sendSms(notificationDto);
                break;
            default:
                notificationTransactionId = null;
                break;
        }

        if (notificationTransactionId == null) {
            throw new IOException("notification transaction id is null");
        }

        notificationDto.setNotificationState(NotificationState.PENDING);

        return notificationTransactionId;
    }

}
