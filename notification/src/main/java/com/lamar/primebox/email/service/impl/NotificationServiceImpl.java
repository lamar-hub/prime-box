package com.lamar.primebox.email.service.impl;

import com.lamar.primebox.email.dto.NotificationDto;
import com.lamar.primebox.email.dto.SendNotificationDto;
import com.lamar.primebox.email.model.Notification;
import com.lamar.primebox.email.repo.NotificationDao;
import com.lamar.primebox.email.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final ModelMapper modelMapper;

    public NotificationServiceImpl(NotificationDao notificationDao, ModelMapper modelMapper) {
        this.notificationDao = notificationDao;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void saveNotification(SendNotificationDto sendNotificationDto) {
        final Notification notification = modelMapper.map(sendNotificationDto, Notification.class);

        notificationDao.saveNotification(notification);
    }

    @Transactional
    @Override
    public List<NotificationDto> getNotificationChunk(Integer chunk) {
        final List<Notification> notificationChunk = notificationDao.getNotificationChunk(chunk);

        return notificationChunk
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void removeNotification(NotificationDto notificationDto) {
        final Notification notification = modelMapper.map(notificationDto, Notification.class);
        
        notificationDao.deleteNotification(notification);
    }

    @Override
    public void updateNotification(NotificationDto notificationDto) {
        final Notification notification = modelMapper.map(notificationDto, Notification.class);
        
        notificationDao.updateNotification(notification);
    }
}
