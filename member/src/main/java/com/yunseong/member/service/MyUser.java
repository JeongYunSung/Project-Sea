package com.yunseong.member.service;

import com.yunseong.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;

@Getter
public class MyUser extends User {

    private Long id;

    public MyUser(Member member) {
        super(member.getUsername(), member.getPassword(), Arrays.asList(new SimpleGrantedAuthority(member.getPermission().getValue())));
        this.id = member.getId();
    }
}
