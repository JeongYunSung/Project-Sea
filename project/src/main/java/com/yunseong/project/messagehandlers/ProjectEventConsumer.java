package com.yunseong.project.messagehandlers;

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
                .build();
    }

    private void startProjectSaga(DomainEventEnvelope<TeamVotedEvent> event) {
        this.projectService.startProject(event.getEvent().getProjectId());
    }
}
