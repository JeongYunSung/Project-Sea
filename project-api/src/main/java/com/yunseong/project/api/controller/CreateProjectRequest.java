package com.yunseong.project.api.controller;

import com.yunseong.board.api.BoardCategory;
import com.yunseong.project.api.event.ProjectTheme;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateProjectRequest {

    private String subject;
    private String content;
    private BoardCategory category;
    private int minSize;
    private int maxSize;
    private boolean open;
    private LocalDate lastDate;
}
