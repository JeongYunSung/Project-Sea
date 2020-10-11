package com.yunseong.project.domain;

import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.api.event.ProjectApproved;
import com.yunseong.project.api.event.ProjectCreated;
import com.yunseong.project.api.event.ProjectEvent;
import com.yunseong.project.api.event.ProjectMember;
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

    @Embedded
    private ProjectInfo projectInfo;

    private Long weClassId;

    @Embedded
    private ProjectMembers projectMembers;

    @Enumerated(EnumType.STRING)
    private ProjectState projectState;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public Project(String username, ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
        this.projectMembers = new ProjectMembers(List.of(new ProjectMember(username, true)));
        this.projectState = ProjectState.RECRUIT_PENDING;
    }

    public static ResultWithDomainEvents<Project, ProjectEvent> create(String username, ProjectInfo projectInfo) {
        return new ResultWithDomainEvents<>(new Project(username, projectInfo), new ProjectCreated(username));
    }

    public List<ProjectEvent> projectJoined(String username) {
        switch(this.projectState) {
            case RECRUIT_PENDING:
                int size = this.getProjectMembers().getProjectMembers().size();
                if(size -1 == this.projectInfo.getMaxPeople()) {
                    this.projectState = ProjectState.APPROVAL_PENDING;
                }
                this.projectMembers.getProjectMembers().add(new ProjectMember(username, false));
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }

    public List<ProjectEvent> projectApproved() {
        switch (this.projectState) {
            case APPROVAL_PENDING:
                int count = this.projectMembers.isApprovedCount();
                if(this.projectInfo.getMinPeople() <= count && this.projectInfo.getMaxPeople() >= count) {
                    this.projectState = ProjectState.APPROVED;
                    return Collections.singletonList(new ProjectApproved(this.id, this.projectInfo.getSubject(), this.projectMembers.getProjectMembers()));
                }
                return Collections.emptyList();
            default:
                throw new UnsupportedStateTransitionException(this.projectState);
        }
    }
}
