package com.yunseong.team.controller;

import com.yunseong.common.AES256Util;
import com.yunseong.team.domain.Team;
import com.yunseong.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping(value = "/teams", consumes = MediaType.APPLICATION_JSON_VALUE)
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private AES256Util aes256Util;

    @GetMapping(value = "/authorize/accept")
    public ResponseEntity authorizeAcceptTeam(@RequestParam("token") String token) throws GeneralSecurityException, UnsupportedEncodingException {
        String[] decrypt = aes256Util.decrypt(token).split("(AND)");
        if(decrypt.length != 2) return ResponseEntity.badRequest().body("잘못된 토큰값입니다.");
        return ResponseEntity.ok(this.teamService.accept(Long.parseLong(decrypt[0]), decrypt[1]));
    }

    @GetMapping(value = "/authorize/reject")
    public ResponseEntity authorizeRejectTeam(@RequestParam("token") String token) throws GeneralSecurityException, UnsupportedEncodingException {
        String[] decrypt = aes256Util.decrypt(token).split("(AND)");
        if(decrypt.length != 2) return ResponseEntity.badRequest().body("잘못된 토큰값입니다.");
        return ResponseEntity.ok(this.teamService.reject(Long.parseLong(decrypt[0]), decrypt[1]));
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
        return ResponseEntity.ok(new TeamMembersResponse(team.getTeamMembers()));
    }
}
