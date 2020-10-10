package com.yunseong.notification.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long memberId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, updatable = false)
    private String content;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDate createdDate;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDate updatedDate;

    public Notification(Long memberId, String subject, String content) {
        this.memberId = memberId;
        this.subject = subject;
        this.content = content;
        this.isRead = false;
    }
}
