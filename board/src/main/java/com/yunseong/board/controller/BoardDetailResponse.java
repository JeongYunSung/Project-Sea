package com.yunseong.board.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunseong.board.api.BoardCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BoardDetailResponse {

    private long id;
    private String writer;
    private String subject;
    private String content;
    private BoardCategory category;
    private long readCount;
    private long recommendCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
}
