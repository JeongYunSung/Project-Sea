package com.yunseong.project.domain;

import com.yunseong.project.api.event.ProjectDetails;
import com.yunseong.project.api.event.ProjectMember;
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
    private ProjectDetails projectDetails;

    @Embedded
    private ProjectMembers projectMembers;

    @Enumerated(EnumType.STRING)
    private ProjectState projectState = ProjectState.RECRUITING;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public Project(String username, ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
        this.projectMembers = new ProjectMembers(List.of(new ProjectMember(username, true)));
        this.projectState = ProjectState.RECRUITING;
    }

    public static ResultWithEvents<Project> create(String username, ProjectDetails projectDetails) {
        return new ResultWithEvents<>(new Project(username, projectDetails));
    }
}
