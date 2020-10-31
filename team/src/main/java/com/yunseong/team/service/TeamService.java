package com.yunseong.team.service;

import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.api.event.TeamEvent;
import com.yunseong.team.domain.Team;
import com.yunseong.team.domain.TeamDomainEventPublisher;
import com.yunseong.team.domain.TeamRejectException;
import com.yunseong.team.domain.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.BiFunction;

@Service
@Transactional
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamDomainEventPublisher teamDomainEventPublisher;

    public Team createTeam(long projectId, String username, int minSize, int maxSize) {
        return this.teamRepository.save(new Team(projectId, username, minSize, maxSize));
    }

//    public void setProject(long teamId, long projectId) {
//        getTeamByTeamId(teamId).setProject(projectId);
//    }

    public Team updateTeam(long teamId, String username, BiFunction<Team, String, List<TeamEvent>> handler) throws EntityNotFoundException {
        Team team = getTeamByTeamId(teamId);
        isUser(team, username);
        List<TeamEvent> events = handler.apply(team, username);
        this.teamDomainEventPublisher.publish(team, events);
        return team;
    }

    public Team accept(long teamId, String username) throws EntityNotFoundException {
        return updateTeam(teamId, username, Team::memberApprove);
    }

    public Team reject(long teamId, String username) throws EntityNotFoundException {
        return updateTeam(teamId, username, Team::memberReject);
    }

    public Team quitTeam(long teamId, String username) throws EntityNotFoundException {
        return updateTeam(teamId, username, Team::memberQuit);
    }

    public Team joinTeam(long teamId, String username) throws EntityNotFoundException {
        Team team = getTeamByTeamId(teamId);
        this.teamDomainEventPublisher.publish(team, team.join(username));
        return team;
    }

    public boolean cancel(long teamId) {
        try {
            Team team = this.getTeamByTeamId(teamId);
            team.cancel();
            return true;
        }catch (UnsupportedStateTransitionException e) {
            return false;
        }
    }

    public boolean approveTeam(long teamId) {
        try {
            Team team = this.getTeamByTeamId(teamId);
            team.approveTeam();
            return true;
        } catch(TeamRejectException e) {
            return false;
        }
    }

    public void rejectTeam(long teamId) {
        Team team = this.getTeamByTeamId(teamId);
        team.rejectTeam();
    }

    @Transactional(readOnly = true)
    public Team findTeamMembersByTeamId(long teamId) {
        return this.teamRepository.findFetchByTeamId(teamId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Page<Long> findTeamByUsername(String username, Pageable pageable) {
        return this.teamRepository.findByUsername(username, pageable);
    }

    private void isUser(Team team, String username) throws EntityNotFoundException {
        if (!team.isUser(username)) throw new EntityNotFoundException("해당 유저는 해당 팀에 존재하지 않습니다.");
    }

//    private Team getTeamMembersByProjectId(long projectId) {
//        return this.teamRepository.findByProjectId(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다."));
//    }

    private Team getTeamByTeamId(long teamId) {
        return this.teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다."));
    }
}
