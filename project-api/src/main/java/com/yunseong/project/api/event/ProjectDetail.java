package com.yunseong.project.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectDetail {

    private long teamId;
    private String subject;
    private String content;
}
