package com.yunseong.project.api.controller;

import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.api.event.ProjectTheme;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectDetailResponse {

    private long id;
    private String subject;
    private String content;
    private long teamId;
    private Long weClassId;
    private ProjectTheme projectTheme;
    private ProjectState projectState;
}
