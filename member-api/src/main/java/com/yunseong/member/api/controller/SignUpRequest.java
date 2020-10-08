package com.yunseong.member.api.controller;

public class SignUpRequest {

    private String username;
    private String password;
    private String nickname;

    private SignUpRequest() {}

    public SignUpRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }
}
