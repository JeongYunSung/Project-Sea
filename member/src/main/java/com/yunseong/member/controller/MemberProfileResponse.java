package com.yunseong.member.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunseong.member.api.controller.Permission;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberProfileResponse {

    private String username;
    private String nickname;
    private Permission permission;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
}
