package com.yunseong.notification.service;

import com.yunseong.notification.domain.Notification;
import com.yunseong.notification.domain.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(long memberId, String subject, String content) {
        this.notificationRepository.save(new Notification(memberId, subject, content));
    }

    public Notification findById(long id) {
        return this.notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id + "를 가진 Notification 엔티티가 존재하지 않습니다."));
    }

    public Page<Notification> findAll(Pageable pageable) {
        return this.notificationRepository.findAll(pageable);
    }
}
