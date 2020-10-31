package com.yunseong.member.controller;

import com.yunseong.common.AES256Util;
import com.yunseong.common.NotMatchedCryptException;
import com.yunseong.member.api.controller.SignUpRequest;
import com.yunseong.member.api.controller.MemberResponse;
import com.yunseong.member.domain.Member;
import com.yunseong.member.service.MemberService;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/members", consumes = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;
    private final AES256Util aes256Util;

    @PostMapping(value = "/signup")
    public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpRequest request) {
        ResultWithEvents<Member> result = this.memberService.signUp(request.getUsername(), request.getPassword(), request.getNickname());
        return new ResponseEntity<>(new MemberResponse(result.result.getUsername()), HttpStatus.CREATED);
    }

    @PutMapping(value = "/authenticate")
    public ResponseEntity<Boolean> authentication(@RequestBody MemberAuthenticateRequest request) {
        String[] decrypt = aes256Util.decrypt(request.getToken()).split("이메일인증코드번호:");
        if(decrypt.length != 2)throw new NotMatchedCryptException("잘못된 토큰 값입니다.");
        return ResponseEntity.ok(this.memberService.authenticate(decrypt[1]));
    }

    @GetMapping(value = "/profile/{username}")
    public ResponseEntity<MemberProfileResponse> profile(@PathVariable String username) {
        Member member = this.memberService.findMember(username);
        return ResponseEntity.ok(new MemberProfileResponse(member.getUsername(), member.getNickname(), member.getPermission(), member.getCreatedDate()));
    }

    @PutMapping(value = "/profile/{username}")
    public ResponseEntity<MemberResponse> update(@PathVariable String username, @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(new MemberResponse(this.memberService.changeNickname(username, request.getNickname())));
    }
}
