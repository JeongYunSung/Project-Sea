package com.yunseong.project.history.domain;

import com.yunseong.project.api.event.TeamPermission;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {

    private String username;
    private TeamPermission teamPermission;
}
