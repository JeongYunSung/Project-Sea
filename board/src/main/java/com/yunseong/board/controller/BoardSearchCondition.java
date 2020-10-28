package com.yunseong.board.controller;

import com.yunseong.board.domain.BoardCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BoardSearchCondition {

    private String writer;
    private String subject;
    private BoardCategory category;
}
