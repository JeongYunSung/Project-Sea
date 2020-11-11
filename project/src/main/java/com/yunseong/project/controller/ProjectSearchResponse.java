package com.yunseong.project.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private long id;
    private String subject;
    private BoardCategory category;
    private ProjectState projectState;
    private long recommendCount;
    private boolean isOpen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
}
