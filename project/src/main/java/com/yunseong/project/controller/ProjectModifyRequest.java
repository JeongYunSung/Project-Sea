package com.yunseong.project.controller;

import com.yunseong.project.api.event.ProjectTheme;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectModifyRequest {

    private String subject;
    private String content;
    private ProjectTheme projectTheme;
}