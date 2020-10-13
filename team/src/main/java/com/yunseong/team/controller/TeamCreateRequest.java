package com.yunseong.team.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamCreateRequest {

    private String username;
    private int minSize;
    private int maxSize;
}
