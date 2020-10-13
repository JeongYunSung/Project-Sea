package com.yunseong.project.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeamJoinedRequestEvent implements TeamEvent {

    private Long projectId;
    private List<TeamMember> teamMembers;
}
