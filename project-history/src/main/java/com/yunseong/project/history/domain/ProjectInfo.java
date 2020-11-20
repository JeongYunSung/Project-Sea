package com.yunseong.project.history.domain;

import com.yunseong.project.api.event.ProjectState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectInfo {

    private String projectSubject;
    private String projectContent;
    private ProjectState projectState;
}
