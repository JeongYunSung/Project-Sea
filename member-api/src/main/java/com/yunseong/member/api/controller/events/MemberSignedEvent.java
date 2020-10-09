package com.yunseong.member.api.controller.events;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberSignedEvent implements DomainEvent {

    private String username;
    private String nickname;
}
