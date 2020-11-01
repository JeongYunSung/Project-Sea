package com.yunseong.project.domain;

import com.yunseong.common.AlreadyExistedElementException;
import com.yunseong.common.CannotReviseBoardIfWriterNotWereException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private String writer;

    private Long teamId;

    private Long weClassId;

    @Enumerated(EnumType.STRING)
    private ProjectState projectState;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectTheme projectTheme;

    private boolean isPublic;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "username")
    private Set<String> members = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public Project(String subject, String content, String writer, ProjectTheme projectTheme, boolean isPublic) {
        this.subject = subject;
        this.content = content;
        this.writer = writer;
        this.projectTheme = projectTheme;
        this.projectState = ProjectState.POST_PENDING;
        this.isPublic = isPublic;
    }

    public static ResultWithDomainEvents<Project, ProjectEvent> create(String subject, String content, String writer, ProjectTheme projectTheme, boolean isPublic) {
        return new ResultWithDomainEvents<>(new Project(subject, content, writer, projectTheme, isPublic), new ProjectCreatedEvent(new ProjectDetail(subject, content)));
    }

    public void addMember(String username) {
        if(!this.members.add(username)) throw new AlreadyExistedElementException("이미 당신은 팀에 속해있습니다.");
    }

    public void removeMember(String username) {
        this.members.remove(username);
    }

    public List<ProjectEvent> revise() {
        if (this.projectState == ProjectState.POSTED) {
            this.projectState = ProjectState.REVISION_PENDING;
            return Collections.emptyList();
        }
        throw new UnsupportedStateTransitionException(this.projectState);
    }

    public List<ProjectEvent> revised(String username, ProjectRevision projectRevision) {
        if (this.projectState == ProjectState.POSTED) {
            if(!username.equals(this.writer)) throw new CannotReviseBoardIfWriterNotWereException("작성자가 아니면 수정할 수 없습니다");
            this.subject = projectRevision.getSubject();
            this.content = projectRevision.getContent();
            this.projectTheme = projectRevision.getTheme();
            this.isPublic = projectRevision.isPublic();
            return Collections.emptyList();
        }
        throw new UnsupportedStateTransitionException(this.projectState);
    }

    public List<ProjectEvent> cancel() {
        if (this.projectState == ProjectState.POSTED) {
            this.projectState = ProjectState.CANCEL_PENDING;
            return Collections.emptyList();
        }
        throw new UnsupportedStateTransitionException(this.projectState);
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
        if (this.projectState == ProjectState.POSTED) {
            this.projectState = ProjectState.CLOSED;
            return Collections.emptyList();
        }
        throw new UnsupportedStateTransitionException(this.projectState);
    }

    public List<ProjectEvent> reject() {
        if (this.projectState == ProjectState.CLOSED) {
            this.projectState = ProjectState.REJECTED;
            return Collections.emptyList();
        }
        throw new UnsupportedStateTransitionException(this.projectState);
    }

    public List<ProjectEvent> start() {
        if (this.projectState == ProjectState.CLOSED) {
            this.projectState = ProjectState.STARTED;
            return Collections.emptyList();
        }
        throw new UnsupportedStateTransitionException(this.projectState);
    }

    public void registerTeam(long teamId) {
        if (this.projectState == ProjectState.POST_PENDING) {
            this.teamId = teamId;
            return;
        }
        throw new UnsupportedStateTransitionException(this.projectState);
    }

    public void registerWeClass(long weClassId) {
        if (this.projectState == ProjectState.CLOSED) {
            this.weClassId = weClassId;
            return;
        }
        throw new UnsupportedStateTransitionException(this.projectState);
    }

    public List<ProjectEvent> modifyProject(String subject, String content, ProjectTheme projectTheme) {
        if (this.projectState == ProjectState.POSTED) {
            this.subject = subject;
            this.content = content;
            this.projectTheme = projectTheme;
            return Collections.emptyList();
        }
        throw new UnsupportedStateTransitionException(this.projectState);
    }
}
