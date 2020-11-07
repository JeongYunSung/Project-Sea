package com.yunseong.project.api.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TeamJoinedEvent implements TeamEvent {

    private Long projectId;
    private TeamMemberDetail teamMemberDetail;
    private List<TeamMemberDetail> teamMembers;
}
