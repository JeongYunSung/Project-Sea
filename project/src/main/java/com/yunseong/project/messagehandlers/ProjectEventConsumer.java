package com.yunseong.project.messagehandlers;

import com.yunseong.project.api.event.TeamJoinedRequestEvent;
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
                .onEvent(TeamJoinedRequestEvent.class, this::joinMember)
                .onEvent(TeamQuitEvent.class, this::quitMember)
                .build();
    }

    private void startProjectSaga(DomainEventEnvelope<TeamVotedEvent> event) {
        this.projectService.startProject(event.getEvent().getProjectId(), event.getEvent().getTeamId());
    }

    private void joinMember(DomainEventEnvelope<TeamJoinedRequestEvent> event) {
        this.projectService.addMember(event.getEvent().getProjectId(), event.getEvent().getUsername());
    }

    private void quitMember(DomainEventEnvelope<TeamQuitEvent> event) {
        this.projectService.removeMember(event.getEvent().getProjectId(), event.getEvent().getUsername());
    }
}
