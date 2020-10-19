package com.yunseong.project.api.controller;

import com.yunseong.project.api.event.TeamMemberDetail;
import com.yunseong.project.api.event.TeamMemberState;
import com.yunseong.project.api.event.TeamState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TeamDetailResponse {

    private int minSize;
    private int maxSize;
    private TeamState teamState;
    private List<TeamMemberDetail> members;
}
