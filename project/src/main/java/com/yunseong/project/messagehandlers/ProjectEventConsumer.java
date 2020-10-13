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
                .onEvent(TeamVotedEvent.class, this::createWeClassSaga)
                .build();
    }

    private void createWeClassSaga(DomainEventEnvelope<TeamVotedEvent> event) {
        for(int i=0;i<3;i++) {
            if(this.projectService.createWeClass(event.getEvent().getProjectId())) {
                return;
            }
        }
        this.projectService.cancelProject(event.getEvent().getProjectId()); // 나중에 CancelSaga적용예정
    }
}
