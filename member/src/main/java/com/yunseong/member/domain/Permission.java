package com.yunseong.member.domain;

import lombok.Getter;

@Getter
public enum Permission {
    ADMIN("ADMIN"), USER("USER");

    private String value;

    Permission(String value) {
        this.value = value;
    }
}
