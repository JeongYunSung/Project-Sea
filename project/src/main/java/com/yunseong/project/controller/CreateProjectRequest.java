package com.yunseong.project.controller;

import com.yunseong.board.api.BoardCategory;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastDate;
}