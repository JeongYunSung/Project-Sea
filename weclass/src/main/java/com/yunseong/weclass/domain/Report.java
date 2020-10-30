package com.yunseong.weclass.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue
    private Long id;

    private String writer;

    private String subject;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weclass_name")
    private WeClass weClass;

    private boolean isDelete;

    @CreatedDate
    private LocalDateTime createdTime;
    @LastModifiedDate
    private LocalDateTime updatedTime;

    public Report(WeClass weClass, String writer, String subject, String content) {
        this.weClass = weClass;
        this.writer = writer;
        this.subject = subject;
        this.content = content;
        this.isDelete = false;
    }

    public void changeSubject(String subject) {
        this.subject = subject;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.isDelete = true;
    }
}
