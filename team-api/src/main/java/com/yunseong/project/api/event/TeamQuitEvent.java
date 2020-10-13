package com.yunseong.project.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamQuitEvent implements TeamEvent {

    private long projectId;
    private String username;
}
