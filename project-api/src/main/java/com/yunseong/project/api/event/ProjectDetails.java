package com.yunseong.project.api.event;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class ProjectDetails {

    private String subject;
    private String content;
    private Integer minPeople;
    private Integer maxPeople;

    public ProjectDetails(String subject, String content, Integer maxPeople) {
        this(subject, content, maxPeople, maxPeople);
    }

    public ProjectDetails(String subject, String content, Integer minPeople, Integer maxPeople) {
        this.subject = subject;
        this.content = content;
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
    }
}
