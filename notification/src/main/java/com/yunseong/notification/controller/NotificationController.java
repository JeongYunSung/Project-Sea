package com.yunseong.notification.controller;

import com.yunseong.notification.domain.Notification;
import com.yunseong.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<PagedModel<NotificationBasicResponse>> findByUsername(Principal principal, @PageableDefault Pageable pageable) {
        Page<NotificationBasicResponse> page = this.notificationService.findByUsername(principal.getName(), pageable).map(n -> new NotificationBasicResponse(n.getId(), n.getSubject(), n.isRead(), n.getCreatedDate()));
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        return ResponseEntity.ok(PagedModel.of(page.getContent(), pageMetadata));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDetailResponse> findById(@PathVariable long id, Principal principal) {
        Notification notification = this.notificationService.findById(id, principal.getName());
        return ResponseEntity.ok(new NotificationDetailResponse(notification.getSubject(), notification.getContent(), notification.getCreatedDate()));
    }
}
