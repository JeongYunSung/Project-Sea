package com.yunseong.board.controller;

import com.yunseong.board.api.BoardCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BoardCreateRequest {

    private String subject;
    private String content;
    private BoardCategory category;
}
