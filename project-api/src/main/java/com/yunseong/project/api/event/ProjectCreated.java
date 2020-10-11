package com.yunseong.project.api.event;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProjectCreated implements DomainEvent {

    private ProjectDetails projectDetails;
}
