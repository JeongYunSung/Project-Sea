package com.yunseong.project.history.domain;

import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.api.event.ProjectTheme;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectInfo {

    private String projectSubject;
    private ProjectTheme projectTheme;
    private String projectContent;
    private ProjectState projectState;
}
