package com.yunseong.member.api.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    private String username;
    private String password;
    private String nickname;
}
