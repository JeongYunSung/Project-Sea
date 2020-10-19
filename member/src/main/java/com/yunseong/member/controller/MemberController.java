package com.yunseong.member.controller;

import com.yunseong.member.api.controller.SignUpRequest;
import com.yunseong.member.api.controller.MemberResponse;
import com.yunseong.member.domain.Member;
import com.yunseong.member.service.MemberService;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpRequest request) {
        ResultWithEvents<Member> result = this.memberService.signUp(request.getUsername(), request.getPassword(), request.getNickname());
        return new ResponseEntity<>(new MemberResponse(result.result.getUsername()), HttpStatus.CREATED);
    }

    @GetMapping(value = "/profile/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberProfileResponse> profile(@PathVariable String username) {
        Member member = this.memberService.findMember(username);
        return ResponseEntity.ok(new MemberProfileResponse(member.getUsername(), member.getNickname(), member.getPermission(), member.getCreatedDate()));
    }

    @PutMapping(value = "/profile/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberResponse> update(@PathVariable String username, @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(new MemberResponse(this.memberService.changeNickname(username, request.getNickname())));
    }
}
