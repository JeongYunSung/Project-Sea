package com.yunseong.team.controller;

import com.yunseong.project.api.event.TeamMemberDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeamMembersResponse {

    private List<TeamMemberDetail> teamMembers;
}
