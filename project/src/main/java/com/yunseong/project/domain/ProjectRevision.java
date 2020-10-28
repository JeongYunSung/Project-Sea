package com.yunseong.project.domain;

import com.yunseong.project.api.event.ProjectTheme;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectRevision {

    private String subject;
    private String content;
    private ProjectTheme projectTheme;
}
