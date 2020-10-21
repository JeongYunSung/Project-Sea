package com.yunseong.notification.messagehandlers;

import com.yunseong.common.AES256Util;
import com.yunseong.member.api.controller.events.MemberSignedEvent;
import com.yunseong.notification.service.NotificationService;
import com.yunseong.project.api.event.TeamAuthorizeVoteRequestedEvent;
import com.yunseong.project.api.event.TeamJoinedRequestEvent;
import com.yunseong.project.api.event.TeamMemberDetail;
import com.yunseong.project.api.event.TeamQuitEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationServiceEventConsumer {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AES256Util aes256Util;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("com.yunseong.member.domain.Member")
                .onEvent(MemberSignedEvent.class, this::createNotificationOfNewMember)
                .andForAggregateType("com.yunseong.team.domain.Team")
                .onEvent(TeamJoinedRequestEvent.class, this::createNotificationOfNewTeamMember)
                .onEvent(TeamAuthorizeVoteRequestedEvent.class, this::createNotificationTeamAuthorizeRequest)
                .onEvent(TeamQuitEvent.class, this::createNotificationTeamQuit)
                .build();
    }

    private void createNotificationOfNewMember(DomainEventEnvelope<MemberSignedEvent> event) {
        this.sendNotification(event.getEvent().getUsername(), "가입을 축하드립니다 !",
                event.getEvent().getNickname() + "님 만나서 반갑습니다.이용중 불편하신점은 123dbstjd@naver.com으로 이메일 남겨주시면 감사하겠습니다");
    }

    private void createNotificationOfNewTeamMember(DomainEventEnvelope<TeamJoinedRequestEvent> event) {
        event.getEvent().getTeamMembers().forEach(m ->
                this.sendNotification(m.getUsername(), "[" + event.getEvent().getProjectId() + "]프로젝트 알림", "신규회원이 가입하였습니다 !"));
    }

    private void createNotificationTeamAuthorizeRequest(DomainEventEnvelope<TeamAuthorizeVoteRequestedEvent> event) {
        for (TeamMemberDetail teamMember : event.getEvent().getTeamMembers()) {
            try {
                this.sendNotification(teamMember.getUsername(), "[" + event.getEvent().getProjectId() + "]프로젝트 알림", aes256Util.encrypt(event.getAggregateId() + "SPLIT" + teamMember.getUsername()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createNotificationTeamQuit(DomainEventEnvelope<TeamQuitEvent> event) {
        this.sendNotification(event.getEvent().getUsername(), "[" + event.getEvent().getProjectId() + "]프로젝트 알림",
                event.getEvent().getUsername() + "님이 프로젝트를 탈퇴하였습니다 ㅠ.ㅠ");
    }

    private void sendNotification(String username, String subject, String content) {
        this.notificationService.createNotification(username, subject, content);
//        this.notificationService.sendMail(username, subject, content);
    }
}
