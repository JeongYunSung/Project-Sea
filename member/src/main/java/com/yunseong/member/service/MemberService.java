package com.yunseong.member.service;

import com.yunseong.member.domain.Member;
import com.yunseong.member.domain.MemberRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResultWithEvents<Member> signUp(String username, String password, String nickname) {
        ResultWithEvents<Member> rwe = Member.create(username, this.passwordEncoder.encode(password), nickname);
        this.memberRepository.save(rwe.result);
        domainEventPublisher.publish(Member.class, rwe.result.getId(), rwe.events);
        return rwe;
    }

    @Transactional(readOnly = true)
    public Member findMember(String username) {
        return this.memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지않습니다."));
    }

    public String changeNickname(String username, String nickname) {
        Member member = this.memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지않습니다."));
        member.changeNickname(nickname);
        return member.getUsername();
    }
}
