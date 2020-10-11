package com.yunseong.project.api.event;

import lombok.Getter;

import java.util.List;

@Getter
public class ProjectApproved implements ProjectEvent {

    private long id;
    private String subject;
    private List<ProjectMember> projectMembers;

    public ProjectApproved(long id, String subject, List<ProjectMember> projectMembers) {
        this.id = id;
        this.subject = subject;
        this.projectMembers = projectMembers;
    }
}
