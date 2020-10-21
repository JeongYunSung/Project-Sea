package com.yunseong.weclass.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WeClassReportDetailResponse {

    private String writer;
    private String subject;
    private String content;
    private LocalDateTime createdTime;
}
