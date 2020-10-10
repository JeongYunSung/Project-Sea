package com.yunseong.notification.messagehandlers;

import com.yunseong.member.api.controller.events.MemberSignedEvent;
import com.yunseong.notification.service.NotificationService;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationServiceEventConsumer {

    @Autowired
    private NotificationService notificationService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("com.yunseong.member.domain.Member")
                .onEvent(MemberSignedEvent.class, this::createNotificationOfNewMember)
                .build();
    }

    private void createNotificationOfNewMember(DomainEventEnvelope<MemberSignedEvent> event) {
        long id = Long.parseLong(event.getAggregateId());
        this.notificationService.createNotification(id, "가입을 축하드립니다 !",
                event.getEvent().getNickname() + "님 만나서 반갑습니다.\\n이용중 불편하신점은 123dbstjd@naver.com으로 이메일 남겨주시면 감사하겠습니다");
    }
}
