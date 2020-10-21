package com.yunseong.weclass.domain;

import com.yunseong.weclass.api.events.WeClassCreatedEvent;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeClass {

    @Id
    @GeneratedValue
    private Long id;

    private long projectId;

    private String notice;

    @OneToMany(mappedBy = "weClass", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public WeClass(long projectId) {
        this.projectId = projectId;
        this.notice = "WeClass에 오신걸 환영합니다.";
    }

    public static ResultWithEvents<WeClass> create(long projectId) {
        return new ResultWithEvents<>(new WeClass(projectId), new WeClassCreatedEvent(projectId));
    }
}
