package com.yunseong.team.domain;

import com.yunseong.project.api.event.TeamEvent;
import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;

public class TeamDomainEventPublisher extends AbstractAggregateDomainEventPublisher<Team, TeamEvent> {

    public TeamDomainEventPublisher(DomainEventPublisher eventPublisher) {
        super(eventPublisher, Team.class, Team::getId);
    }
}
