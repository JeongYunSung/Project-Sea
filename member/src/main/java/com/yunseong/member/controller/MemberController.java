package com.yunseong.member.controller;

import com.yunseong.member.api.controller.SignUpRequest;
import com.yunseong.member.api.controller.SignUpResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @PostMapping(name = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        return null;
    }
}
