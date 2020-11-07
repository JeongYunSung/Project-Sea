package com.yunseong.project.controller;

import com.yunseong.board.api.BoardCategory;
import com.yunseong.project.api.event.ProjectState;
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
    private BoardCategory boardCategory;
    private ProjectState projectState;
    private LocalDateTime projectPostedTime;
}
