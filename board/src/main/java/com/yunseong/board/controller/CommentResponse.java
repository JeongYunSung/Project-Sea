package com.yunseong.board.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunseong.board.domain.CommentState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CommentResponse {

    private long id;
    private String writer;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    private CommentState commentState;
}
