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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

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
        try {
            this.sendNotification(event.getEvent().getUsername(), "가입을 축하드립니다 !",
                    event.getEvent().getNickname() + "님 만나서 반갑습니다.|" +
                            "해당 서비스를 원활하게 이용하기 위해선 이메일 인증을 진행해주셔야 합니다|" +
                            "코드번호 : " + aes256Util.encrypt("이메일인증코드번호:" + event.getEvent().getUsername()) + "'/>|" +
                            "위 코드를 클릭해 사이트에 접속하시면 인증이 완료됩니다|" +
                            "이외의 불편하신점 혹은 궁금하신점이 있으시면 123dbstjd@naver.com으로 이메일 남겨주시면 감사하겠습니다.", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNotificationOfNewTeamMember(DomainEventEnvelope<TeamJoinedRequestEvent> event) {
        event.getEvent().getTeamMembers().forEach(m ->
                this.sendNotification(m.getUsername(), "(" + event.getEvent().getProjectId() + ")프로젝트 알림", event.getEvent().getUsername() + "님이 프로젝트를 가입하였습니다.", false));
    }

    private void createNotificationTeamAuthorizeRequest(DomainEventEnvelope<TeamAuthorizeVoteRequestedEvent> event) {
        for (TeamMemberDetail teamMember : event.getEvent().getTeamMembers()) {
            try {
                this.sendNotification(teamMember.getUsername(), "(" + event.getEvent().getProjectId() + ")프로젝트 알림", "프로젝트 시작을 위한 인증번호 코드 발급안내|코드번호 : "+ aes256Util.encrypt(event.getAggregateId() + "SPLIT" + teamMember.getUsername()), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createNotificationTeamQuit(DomainEventEnvelope<TeamQuitEvent> event) {
        event.getEvent().getTeamMembers().forEach(m ->
                this.sendNotification(m.getUsername(), "(" + event.getEvent().getProjectId() + ")프로젝트 알림",
                        event.getEvent().getUsername() + "님이 프로젝트를 탈퇴하였습니다.", false));
    }

    private void sendNotification(String username, String subject, String content, boolean mail) {
        this.notificationService.createNotification(username, subject, content);
        String prefix = "[Project-Sea]";
        if(mail) {
            this.notificationService.sendMail(username, prefix + subject,
                    "<div style=\"font-family: Arial, Helvetica, sans-serif; text-align: center\">\n" +
                            "  <h1>" + subject + "</h1>\n" +
                            "  <hr>\n<p>" + String.join("</p><p>", content.split("\\|")) +
                            "</p></div>");
        }
    }
}
