package com.yunseong.project.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProjectCreated implements ProjectEvent {

    private String leaderUsername;
}
