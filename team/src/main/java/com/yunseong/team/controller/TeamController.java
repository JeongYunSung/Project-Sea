package com.yunseong.team.controller;

import com.yunseong.common.AES256Util;
import com.yunseong.common.NotMatchedCryptException;
import com.yunseong.project.api.controller.TeamBasicResponse;
import com.yunseong.project.api.controller.TeamDetailResponse;
import com.yunseong.project.api.controller.TeamVoteRequest;
import com.yunseong.project.api.event.TeamMemberDetail;
import com.yunseong.team.domain.Team;
import com.yunseong.team.domain.TeamMember;
import com.yunseong.team.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/teams", consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final AES256Util aes256Util;

    @GetMapping(value = "/search")
    public ResponseEntity<PagedModel<TeamBasicResponse>> findTeamByUsername(@ModelAttribute TeamSearchCondition teamSearchCondition, @PageableDefault Pageable pageable) {
        Page<TeamBasicResponse> page = this.teamService.findTeamByUsername(teamSearchCondition.getUsername(), pageable).map(TeamBasicResponse::new);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());

        return ResponseEntity.ok(PagedModel.of(page.getContent(), pageMetadata));
    }

    @PutMapping(value = "/join/accept")
    public ResponseEntity<?> authorizeAcceptTeam(@RequestBody TeamVoteRequest teamVoteRequest) throws Exception {
        String[] decrypt = aes256Util.decrypt(teamVoteRequest.getToken()).split("SPLIT");
        if(decrypt.length != 2) throw new NotMatchedCryptException("잘못된 토큰 값입니다.");
        this.teamService.accept(Long.parseLong(decrypt[0]), decrypt[1]);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/join/reject")
    public ResponseEntity<?> authorizeRejectTeam(@RequestBody TeamVoteRequest teamVoteRequest) throws Exception {
        String[] decrypt = aes256Util.decrypt(teamVoteRequest.getToken()).split("SPLIT");
        if(decrypt.length != 2) throw new NotMatchedCryptException("잘못된 토큰 값입니다.");
        this.teamService.reject(Long.parseLong(decrypt[0]), decrypt[1]);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/quit/{id}")
    public ResponseEntity<TeamJoinResponse> quitTeam(@PathVariable Long id, @RequestBody TeamUpdateRequest teamUpdateRequest) {
        Team team = this.teamService.quitTeam(id, teamUpdateRequest.getUsername());
        return ResponseEntity.ok(new TeamJoinResponse(team.getId()));
    }

    @PutMapping(value = "/join/{id}")
    public ResponseEntity<TeamJoinResponse> joinTeam(@PathVariable Long id, @RequestBody TeamUpdateRequest teamUpdateRequest) {
        Team team = this.teamService.joinTeam(id, teamUpdateRequest.getUsername());
        return ResponseEntity.ok(new TeamJoinResponse(team.getId()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TeamDetailResponse> findTeamMembers(@PathVariable Long id) {
        Team team = this.teamService.findTeamMembersByTeamId(id);
        return ResponseEntity.ok(new TeamDetailResponse(team.getMinSize(), team.getMaxSize(), team.getTeamState(), team.getTeamMembers().stream().map(TeamMember::getTeamMemberDetail).collect(Collectors.toList())));
    }
}
