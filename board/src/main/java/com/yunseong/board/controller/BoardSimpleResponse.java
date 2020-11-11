package com.yunseong.board.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunseong.board.api.BoardCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BoardSimpleResponse {

    private long id;
    private String subject;
    private String content;
    private BoardCategory category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
}
