package com.yunseong.project.api.controller;

import com.yunseong.project.api.event.TeamMemberDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeamDetailResponse {

    private int minSize;
    private int maxSize;
    private List<TeamMemberDetail> members;
}
