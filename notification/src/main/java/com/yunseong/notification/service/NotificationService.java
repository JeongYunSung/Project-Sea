package com.yunseong.notification.service;

import com.yunseong.notification.domain.Notification;
import com.yunseong.notification.domain.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(long memberId, String content) {
        this.notificationRepository.save(new Notification(memberId, content));
    }
}
