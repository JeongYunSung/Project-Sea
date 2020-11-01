package com.yunseong.project.api.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TeamJoinedRequestEvent implements TeamEvent {

    private Long projectId;
    private String username;
    private List<TeamMemberDetail> teamMembers;
}
