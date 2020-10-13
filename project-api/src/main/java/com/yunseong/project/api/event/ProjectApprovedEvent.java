package com.yunseong.project.api.event;

import lombok.Getter;

@Getter
public class ProjectApprovedEvent implements ProjectEvent {

    private long id;
    private String subject;

    public ProjectApprovedEvent(long id, String subject) {
        this.id = id;
        this.subject = subject;
    }
}
