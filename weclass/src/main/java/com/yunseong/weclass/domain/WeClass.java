package com.yunseong.weclass.domain;

import com.yunseong.weclass.api.events.WeClassCreatedEvent;
import io.eventuate.tram.events.publisher.ResultWithEvents;
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
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WeClass {

    @Id
    @GeneratedValue
    @Column(name = "weclass_id")
    private Long id;

    private long projectId;

    private String notice;

    private boolean isDelete;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public WeClass(long projectId) {
        this.projectId = projectId;
        this.notice = "WeClass에 오신걸 환영합니다.";
        this.isDelete = false;
    }

    public static ResultWithEvents<WeClass> create(long projectId) {
        return new ResultWithEvents<>(new WeClass(projectId), new WeClassCreatedEvent(projectId));
    }
}
