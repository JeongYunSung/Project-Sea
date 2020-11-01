package com.yunseong.team.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCreateRequest {

    private String username;
    private int minSize;
    private int maxSize;
}
