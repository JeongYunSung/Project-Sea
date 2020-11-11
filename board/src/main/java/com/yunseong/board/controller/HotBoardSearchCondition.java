package com.yunseong.board.controller;

import com.yunseong.board.api.BoardCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class HotBoardSearchCondition {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxDate;
    private BoardCategory category;
    private int size;
}
