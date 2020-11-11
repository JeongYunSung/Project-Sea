package com.yunseong.project.controller;

import com.yunseong.board.api.BoardCategory;
import com.yunseong.project.api.event.ProjectState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class HotProjectSearchResponse {

    private long id;
    private String subject;
    private BoardCategory category;
    private ProjectState projectState;
    private long recommendCount;
}
