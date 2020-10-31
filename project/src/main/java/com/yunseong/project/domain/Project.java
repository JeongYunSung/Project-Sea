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
    @Column(name = "project_id")
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String content;

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

    public Project(String subject, String content, ProjectTheme projectTheme) {
        this.subject = subject;
        this.content = content;
        this.projectTheme = projectTheme;
        this.projectState = ProjectState.POST_PENDING;
    }

    public static ResultWithDomainEvents<Project, ProjectEvent> create(String subject, String content, ProjectTheme projectTheme) {
        return new ResultWithDomainEvents<>(new Project(subject, content, projectTheme), new ProjectCreatedEvent(new ProjectDetail(subject, content)));
    }

    public List<ProjectEvent> revise() {
        switch (this.projectState) {
            case POSTED:
                this.projectState = ProjectState.REVISION_PENDING;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> revised(ProjectRevision projectRevision) {
        switch (this.projectState) {
            case POSTED:
                this.projectState = ProjectState.POSTED;
                this.subject = projectRevision.getSubject();
                this.content = projectRevision.getContent();
                this.projectTheme = projectRevision.getTheme();
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> cancel() {
        switch (this.projectState) {
            case POSTED:
                this.projectState = ProjectState.CANCEL_PENDING;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> undoCancelOrPostedOrRevision() {
        switch (this.projectState) {
            case CANCEL_PENDING: case POST_PENDING: case REVISION_PENDING:
                this.projectState = ProjectState.POSTED;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> cancelled() {
        switch (this.projectState) {
            case CANCEL_PENDING: case POST_PENDING:
                this.projectState = ProjectState.CANCELLED;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
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

    public List<ProjectEvent> registerTeam(long teamId) {
        switch(this.projectState) {
            case POST_PENDING:
                this.teamId = teamId;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> registerWeClass(long weClassId) {
        switch(this.projectState) {
            case CLOSED:
                this.weClassId = weClassId;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> modifyProject(String subject, String content, ProjectTheme projectTheme) {
        switch (this.projectState) {
            case POSTED:
                this.subject = subject;
                this.content = content;
                this.projectTheme = projectTheme;
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }
}
