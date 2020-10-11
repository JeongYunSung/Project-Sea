package com.yunseong.project.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class ProjectInfo {

    private String subject;
    private String content;
    private Integer minPeople;
    private Integer maxPeople;

    public ProjectInfo(String subject, String content, Integer maxPeople) {
        this(subject, content, maxPeople, maxPeople);
    }

    public ProjectInfo(String subject, String content, Integer minPeople, Integer maxPeople) {
        this.subject = subject;
        this.content = content;
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
    }
}
