package com.lamar.primebox.notification.service.impl;

import com.lamar.primebox.notification.dto.NotificationDto;
import com.lamar.primebox.notification.dto.NotificationWebhookDto;
import com.lamar.primebox.notification.dto.SendNotificationDto;
import com.lamar.primebox.notification.model.Notification;
import com.lamar.primebox.notification.model.NotificationState;
import com.lamar.primebox.notification.repo.NotificationDao;
import com.lamar.primebox.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final ModelMapper modelMapper;

    public NotificationServiceImpl(NotificationDao notificationDao, @Qualifier("sgModelMapper") ModelMapper modelMapper) {
        this.notificationDao = notificationDao;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void saveNotification(SendNotificationDto sendNotificationDto) {
        final Notification notification = modelMapper.map(sendNotificationDto, Notification.class);

        notification.setNotificationState(NotificationState.INIT);

        notificationDao.saveNotification(notification);
    }

    @Transactional
    @Override
    public List<NotificationDto> getNotificationsByState(NotificationState notificationState) {
        final List<Notification> initNotifications = notificationDao.getNotificationsByState(notificationState);

        return initNotifications
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateNotification(NotificationDto notificationDto) {
        final Notification notification = modelMapper.map(notificationDto, Notification.class);

        notificationDao.updateNotification(notification);
    }

    @Transactional
    @Override
    public void updateWebhookNotification(NotificationWebhookDto notificationWebhookDto) {
        final Notification notification = notificationDao.getNotificationByTransactionId(notificationWebhookDto.getTransactionId());

        if (notification == null) {
            throw new RuntimeException("there is no transaction");
        }

        if (notification.getNotificationState() != NotificationState.PENDING) {
            throw new RuntimeException("transaction not in PENDING state");
        }

        notification.setNotificationState(notificationWebhookDto.getNotificationState());
        notificationDao.updateNotification(notification);
    }
}
