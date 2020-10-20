package com.yunseong.project.controller;

import com.yunseong.project.api.event.ProjectState;
import com.yunseong.project.api.event.ProjectTheme;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectSearchResponse {

    private long projectId;
    private String subject;
    private ProjectTheme projectTheme;
    private ProjectState projectState;
    private LocalDateTime projectPostedTime;
}
