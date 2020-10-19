package com.yunseong.weclass.api.events;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WeClassCreatedEvent implements DomainEvent {

    private long projectId;
}
