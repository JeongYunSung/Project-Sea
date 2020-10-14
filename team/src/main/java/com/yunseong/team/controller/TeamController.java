package com.yunseong.team.controller;

import com.yunseong.common.AES256Util;
import com.yunseong.project.api.controller.TeamBasicResponse;
import com.yunseong.project.api.controller.TeamDetailResponse;
import com.yunseong.project.api.event.TeamMemberDetail;
import com.yunseong.team.domain.Team;
import com.yunseong.team.domain.TeamMember;
import com.yunseong.team.service.TeamService;
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
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private AES256Util aes256Util;

    @GetMapping(value = "/search")
    public ResponseEntity<PagedModel> findTeamByUsername(@RequestBody String username, @PageableDefault Pageable pageable) {
        Page<TeamBasicResponse> page = this.teamService.findTeamByUsername(username, pageable).map(TeamBasicResponse::new);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());

        return ResponseEntity.ok(PagedModel.of(page.getContent(), pageMetadata));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TeamDetailResponse> findTeamByProjectId(@PathVariable Long id) {
        Team team = this.teamService.findTeamMembersByProjectId(id);
        return ResponseEntity.ok(new TeamDetailResponse(team.getMinSize(), team.getMaxSize(), team.getTeamMembers().stream().map(TeamMember::getTeamMemberDetail).collect(Collectors.toList())));
    }

    @PutMapping(value = "/authorize/accept")
    public ResponseEntity authorizeAcceptTeam(@RequestBody String token) throws GeneralSecurityException, UnsupportedEncodingException {
        String[] decrypt = aes256Util.decrypt(token).split("(AND)");
        if(decrypt.length != 2) return ResponseEntity.badRequest().body("잘못된 토큰값입니다.");
        this.teamService.accept(Long.parseLong(decrypt[0]), decrypt[1]);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/authorize/reject")
    public ResponseEntity authorizeRejectTeam(@RequestBody String token) throws GeneralSecurityException, UnsupportedEncodingException {
        String[] decrypt = aes256Util.decrypt(token).split("(AND)");
        if(decrypt.length != 2) return ResponseEntity.badRequest().body("잘못된 토큰값입니다.");
        this.teamService.reject(Long.parseLong(decrypt[0]), decrypt[1]);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<TeamCreateResponse> createTeam(@RequestBody TeamCreateRequest teamCreateRequest) {
        return new ResponseEntity<>(new TeamCreateResponse(this.teamService.createTeam(teamCreateRequest.getUsername(), teamCreateRequest.getMinSize(), teamCreateRequest.getMaxSize()).getId()), HttpStatus.CREATED);
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

    @GetMapping(value = "/join/{id}")
    public ResponseEntity<TeamMembersResponse> findTeamMembers(@PathVariable Long id) {
        Team team = this.teamService.findTeamMembersByTeamId(id);
        return ResponseEntity.ok(new TeamMembersResponse(team.getTeamMembers().stream().map(TeamMember::getTeamMemberDetail).collect(Collectors.toList())));
    }
}
