package com.yunseong.member.controller;

import com.yunseong.member.domain.Permission;
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
    private LocalDateTime createdTime;
}
