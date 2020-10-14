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

    public void createNotification(String username, String subject, String content) {
        this.notificationRepository.save(new Notification(username, subject, content));
    }

    @Transactional(readOnly = true)
    public Page<Notification> findByUsername(String username, Pageable pageable) {
        return this.notificationRepository.findByUsername(username, pageable);
    }

    public Notification findById(long id) {
        Notification notification = this.notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id + "번호를 가진 Notification 엔티티가 존재하지 않습니다."));
        if(!notification.isRead()) {
            notification.read();
        }
        return notification;
    }
}
