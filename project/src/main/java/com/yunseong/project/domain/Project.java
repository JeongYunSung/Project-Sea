package com.yunseong.project.domain;

import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.api.event.*;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long teamId;

    private Long weClassId;

    @Enumerated(EnumType.STRING)
    private ProjectState projectState;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectTheme projectTheme;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public Project(long teamId, String subject, String content, ProjectTheme projectTheme) {
        this.teamId = teamId;
        this.subject = subject;
        this.content = content;
        this.projectTheme = projectTheme;
        this.projectState = ProjectState.POSTED;
    }

    public static ResultWithDomainEvents<Project, ProjectEvent> create(long teamId, String subject, String content, ProjectTheme projectTheme) {
        return new ResultWithDomainEvents<>(new Project(teamId, subject, content, projectTheme), new ProjectCreatedEvent(new ProjectDetail(teamId, subject, content)));
    }

    public List<ProjectEvent> close() {
        switch(this.projectState) {
            case POSTED:
                this.projectState = ProjectState.CLOSED;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> reject() {
        switch(this.projectState) {
            case CLOSED:
                this.projectState = ProjectState.REJECTED;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> start() {
        switch(this.projectState) {
            case CLOSED:
                this.projectState = ProjectState.STARTED;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> register(long weClassId) {
        switch(this.projectState) {
            case CLOSED:
                this.weClassId = weClassId;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }
}
