package com.yunseong.board.controller;

import com.yunseong.board.api.BoardCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class HotBoardResponse {

    private long id;
    private String writer;
    private String subject;
    private BoardCategory category;
    private long recommendCount;
}
