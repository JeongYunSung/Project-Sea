package com.yunseong.project.api.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TeamQuitEvent implements TeamEvent {

    private long projectId;
    private String username;
    private List<TeamMemberDetail> teamMembers;
}
