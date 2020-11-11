package com.yunseong.project.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunseong.project.api.event.ProjectState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectDetailResponse {

    private final long id;
    private final long boardId;
    private final long teamId;
    private final Long weClassId;
    private final ProjectState state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private final LocalDateTime createdTime;
}
