package com.yunseong.member.domain;

import com.yunseong.member.api.controller.events.MemberSignedEvent;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username", "nickname"}))
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, updatable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Permission permission;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;
    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedDate;

    public Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.permission = Permission.USER;
    }

    public static ResultWithEvents<Member> create(String username, String password, String nickname) {
        return new ResultWithEvents<>(new Member(username, password, nickname), new MemberSignedEvent(username, nickname));
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
