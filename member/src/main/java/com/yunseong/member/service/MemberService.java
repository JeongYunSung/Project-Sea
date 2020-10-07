package com.yunseong.member.service;

import com.yunseong.member.domain.Member;
import com.yunseong.member.domain.MemberRepository;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public ResultWithEvents<Member> test() {
        return null;
    }
}
