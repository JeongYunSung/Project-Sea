package com.yunseong.project.messagehandlers;

import com.yunseong.board.api.events.BoardAddRecommendEvent;
import com.yunseong.project.api.event.TeamAuthorizeVoteRequestedEvent;
import com.yunseong.project.api.event.TeamJoinedEvent;
import com.yunseong.project.api.event.TeamQuitEvent;
import com.yunseong.project.api.event.TeamVotedEvent;
import com.yunseong.project.service.ProjectService;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectEventConsumer {

    @Autowired
    private ProjectService projectService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("com.yunseong.team.domain.Team")
                .onEvent(TeamVotedEvent.class, this::startProjectSaga)
                .onEvent(TeamJoinedEvent.class, this::joinMember)
                .onEvent(TeamQuitEvent.class, this::quitMember)
                .onEvent(TeamAuthorizeVoteRequestedEvent.class, this::closeProject)
                .andForAggregateType("com.yunseong.board.domain.Board")
                .onEvent(BoardAddRecommendEvent.class, this::boardRecommend)
                .build();
    }

    private void closeProject(DomainEventEnvelope<TeamAuthorizeVoteRequestedEvent> event) {
        this.projectService.closeProject(event.getEvent().getProjectId());
    }

    private void boardRecommend(DomainEventEnvelope<BoardAddRecommendEvent> event) {
        this.projectService.addRecommend(event.getEvent().getBoardId(), event.getEvent().getRecommendTime(), event.getEvent().getValue());
    }

    private void startProjectSaga(DomainEventEnvelope<TeamVotedEvent> event) {
        this.projectService.startProject(event.getEvent().getProjectId(), event.getEvent().getTeamId());
    }

    private void joinMember(DomainEventEnvelope<TeamJoinedEvent> event) {
        this.projectService.addMember(event.getEvent().getProjectId(), event.getEvent().getUsername());
    }

    private void quitMember(DomainEventEnvelope<TeamQuitEvent> event) {
        this.projectService.removeMember(event.getEvent().getProjectId(), event.getEvent().getUsername());
    }
}
