package com.yunseong.weclass.api.events;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeClassCreatedEvent implements DomainEvent {

    private long projectId;
}
