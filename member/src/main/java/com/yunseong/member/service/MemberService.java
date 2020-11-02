package com.yunseong.member.service;

import com.yunseong.common.AlreadyExistedEntityException;
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
        if(this.memberRepository.findByNicknameOrUsername(username, nickname).orElse(null) != null) throw new AlreadyExistedEntityException("이미 해당 아이디 혹은 닉네임을 소유한 사람이 존재합니다");
        ResultWithEvents<Member> rwe = Member.create(username, this.passwordEncoder.encode(password), nickname);
        this.memberRepository.save(rwe.result);
        domainEventPublisher.publish(Member.class, rwe.result.getId(), rwe.events);
        return rwe;
    }

    @Transactional(readOnly = true)
    public boolean isMemberByNickname(String nickname) {
        return this.memberRepository.findByNickname(nickname).orElse(null) != null;
    }

    @Transactional(readOnly = true)
    public boolean isMemberByUsername(String username) {
        return this.memberRepository.findByUsername(username).orElse(null) != null;
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
