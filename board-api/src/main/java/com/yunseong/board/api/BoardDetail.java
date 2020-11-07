package com.yunseong.board.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BoardDetail {

    private String writer;
    private String subject;
    private String content;
    private BoardCategory boardCategory;
    private MultipartFile[] images;
}
