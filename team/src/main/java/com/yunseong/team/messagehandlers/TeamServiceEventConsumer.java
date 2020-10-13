package com.yunseong.team.messagehandlers;

import com.yunseong.project.api.event.ProjectCreatedEvent;
import com.yunseong.team.service.TeamService;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class TeamServiceEventConsumer {

    @Autowired
    private TeamService teamService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("com.yunseong.project.domain.Project")
                .onEvent(ProjectCreatedEvent.class, this::setProject)
                .build();
    }

    private void setProject(DomainEventEnvelope<ProjectCreatedEvent> event) {
        this.teamService.setProject(event.getEvent().getProjectDetail().getTeamId(), Long.parseLong(event.getAggregateId()));
    }
}
