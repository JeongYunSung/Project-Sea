package com.yunseong.project.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeamAuthorizeVoteRequestedEvent implements TeamEvent {

    private long projectId;
    private List<TeamMemberDetail> teamMembers;
}
