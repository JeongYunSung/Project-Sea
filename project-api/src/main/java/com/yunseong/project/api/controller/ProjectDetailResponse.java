package com.yunseong.project.api.controller;

import com.yunseong.project.api.event.ProjectState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectDetailResponse {

    private final long id;
    private final long boardId;
    private final long teamId;
    private final Long weClassId;
    private final ProjectState projectState;
}
