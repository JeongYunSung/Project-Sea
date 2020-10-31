package com.yunseong.project.api.controller;

import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.api.event.ProjectTheme;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectDetailResponse {

    private final long id;
    private final String subject;
    private final String content;
    private final long teamId;
    private final Long weClassId;
    private final ProjectTheme projectTheme;
    private final ProjectState projectState;
}
