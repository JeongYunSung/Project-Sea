package com.yunseong.member.service;

import com.yunseong.member.domain.Member;
import com.yunseong.member.domain.MemberRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final PasswordEncoder passwordEncoder;

    public ResultWithEvents<Member> signUp(String username, String password, String nickname) {
        ResultWithEvents<Member> rwe = Member.create(username, this.passwordEncoder.encode(password), nickname);
        this.memberRepository.save(rwe.result);
        domainEventPublisher.publish(Member.class, rwe.result.getId(), rwe.events);
        return rwe;
    }

    @Transactional(readOnly = true)
    public Member findMember(String username) {
        return getMember(username);
    }

    private Member getMember(String username) {
        return this.memberRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("해당 유저는 존재하지않습니다."));
    }

    public String changeNickname(String username, String nickname) {
        Member member = getMember(username);
        member.changeNickname(nickname);
        return member.getUsername();
    }

    public boolean authenticate(String username) {
        Member member = this.getMember(username);
        if(member.isAuthenticated()) throw new AlreadyAuthenticatedUsernameException();
        member.authenticate();
        return member.isAuthenticated();
    }
}
