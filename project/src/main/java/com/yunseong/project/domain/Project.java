package com.yunseong.project.domain;

import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.api.event.ProjectCreatedEvent;
import com.yunseong.project.api.event.ProjectDetail;
import com.yunseong.project.api.event.ProjectEvent;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    private String subject;

    private String content;

    private Long teamId;

    private Long weClassId;

    @Enumerated(EnumType.STRING)
    private ProjectState projectState;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public Project(long teamId, long weClassId, String subject, String content) {
        this.teamId = teamId;
        this.weClassId = weClassId;
        this.subject = subject;
        this.content = content;
        this.projectState = ProjectState.POSTED;
    }

    public static ResultWithDomainEvents<Project, ProjectEvent> create(long teamId, long weClassId, String subject, String content) {
        return new ResultWithDomainEvents<>(new Project(teamId, weClassId, subject, content), new ProjectCreatedEvent(new ProjectDetail(teamId, subject, content)));
    }

    public void changeWeClassId(long weClassId) {
        this.weClassId = weClassId;
    }

    public List<ProjectEvent> reject() {
        switch(this.projectState) {
            case CLOSED:
                this.projectState = ProjectState.REJECTED;
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> start() {
        switch(this.projectState) {
            case CLOSED:
                this.projectState = ProjectState.STARTED;
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }
}
