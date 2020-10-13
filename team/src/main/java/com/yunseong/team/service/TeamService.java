package com.yunseong.team.service;

import com.yunseong.project.api.event.TeamEvent;
import com.yunseong.project.api.event.TeamPermission;
import com.yunseong.team.domain.Team;
import com.yunseong.team.domain.TeamDomainEventPublisher;
import com.yunseong.team.domain.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.BiFunction;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamDomainEventPublisher teamDomainEventPublisher;

    public Team createTeam(String username, int minSize, int maxSize) {
        return this.teamRepository.save(new Team(username, minSize, maxSize));
    }

    public void setProject(long teamId, long projectId) {
        getTeamByTeamId(teamId).setProject(projectId);
    }

    public Team updateTeam(long teamId, String username, BiFunction<Team, String, List<TeamEvent>> handler) throws EntityNotFoundException {
        Team team = getTeamByTeamId(teamId);
        isUser(team, username);
        List<TeamEvent> events = handler.apply(team, username);
        this.teamDomainEventPublisher.publish(team, events);
        return team;
    }

    public Team accept(long teamId, String username) throws EntityNotFoundException {
        return updateTeam(teamId, username, Team::approve);
    }

    public Team reject(long teamId, String username) throws EntityNotFoundException {
        return updateTeam(teamId, username, Team::reject);
    }

    public Team quitTeam(long teamId, String username) throws EntityNotFoundException {
        return updateTeam(teamId, username, Team::quit);
    }

    public Team joinTeam(long teamId, String username) throws EntityNotFoundException {
        return updateTeam(teamId, username, Team::join);
    }

    @Transactional(readOnly = true)
    public Team findTeamMembersByTeamId(long teamId) {
        return getTeamByTeamId(teamId);
    }

    private void isUser(Team team, String username) throws EntityNotFoundException {
        if (!team.isUser(username)) throw new EntityNotFoundException("해당 유저는 해당 팀에 존재하지 않습니다.");
    }

    private Team getTeamByTeamId(long teamId) {
        return this.teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다."));
    }
}
