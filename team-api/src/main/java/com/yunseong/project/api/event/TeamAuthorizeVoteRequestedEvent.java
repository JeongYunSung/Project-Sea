package com.yunseong.project.api.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TeamAuthorizeVoteRequestedEvent implements TeamEvent {

    private long projectId;
    private List<TeamMemberDetail> teamMembers;
}
