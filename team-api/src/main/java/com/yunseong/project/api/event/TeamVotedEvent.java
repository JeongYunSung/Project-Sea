package com.yunseong.project.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamVotedEvent implements TeamEvent {

    private long projectId;
}
