package com.yunseong.member.api.controller;

public class SignUpResponse {
    private long memberId;

    public SignUpResponse(long memberId) {
        this.memberId = memberId;
    }

    public long getMemberId() {
        return this.memberId;
    }
}
