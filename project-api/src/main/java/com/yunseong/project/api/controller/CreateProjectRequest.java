package com.yunseong.project.api.controller;

import com.yunseong.project.api.event.ProjectTheme;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateProjectRequest {

    private long teamId;
    private String subject;
    private String content;
    private ProjectTheme projectTheme;

    private String username;
    private int minSize;
    private int maxSize;
}
