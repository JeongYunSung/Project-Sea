package com.yunseong.notification.controller;

import com.yunseong.notification.domain.Notification;
import com.yunseong.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> findAll(@PageableDefault Pageable pageable) {
        Page<Notification> page = this.notificationService.findAll(pageable);
        return ResponseEntity.ok(page.map(e -> new NotificationResponse(e.getSubject(), e.getContent())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> findById(@PathVariable Long id) {
        Notification notification = this.notificationService.findById(id);
        return ResponseEntity.ok(new NotificationResponse(notification.getSubject(), notification.getContent()));
    }
}
