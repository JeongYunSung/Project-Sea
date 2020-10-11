package com.yunseong.project.domain;

import com.yunseong.project.api.event.ProjectEvent;
import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;

public class ProjectDomainEventPublisher extends AbstractAggregateDomainEventPublisher<Project, ProjectEvent> {

    public ProjectDomainEventPublisher(DomainEventPublisher eventPublisher) {
        super(eventPublisher, Project.class, Project::getId);
    }
}
