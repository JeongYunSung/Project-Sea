package com.yunseong.board.controller;

import com.querydsl.core.annotations.QueryProjection;
import com.yunseong.board.domain.BoardCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSearchResponse {

    private long id;
    private String subject;
    private String writer;
    private BoardCategory boardCategory;
    private long recommendCount;
    private LocalDateTime createdTime;

    @QueryProjection
    public BoardSearchResponse(long id, String subject, String writer, BoardCategory category, LocalDateTime createdTime, long recommendCount) {
        this.id = id;
        this.subject = subject;
        this.writer = writer;
        this.boardCategory = category;
        this.createdTime = createdTime;
        this.recommendCount = recommendCount;
    }
}
