package com.yunseong.board.controller;

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
    private LocalDateTime createdTime;
    private CommentState commentState;
}
