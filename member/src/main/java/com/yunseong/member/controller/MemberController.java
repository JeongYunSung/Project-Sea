package com.yunseong.member.controller;

import com.yunseong.member.api.controller.SignUpRequest;
import com.yunseong.member.api.controller.SignUpResponse;
import com.yunseong.member.domain.Member;
import com.yunseong.member.service.MemberService;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        ResultWithEvents<Member> result = this.memberService.signUp(request.getUsername(), request.getPassword(), request.getNickname());
        return new ResponseEntity(new SignUpResponse(result.result.getId()), HttpStatus.CREATED);
    }
}
