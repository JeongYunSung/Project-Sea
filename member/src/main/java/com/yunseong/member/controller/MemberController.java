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
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/members", consumes = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;
    private final AES256Util aes256Util;

    @PostMapping(value = "/signup")
    public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpRequest request) throws GeneralSecurityException {
        ResultWithEvents<Member> result = this.memberService.signUp(request.getUsername(), request.getPassword(), request.getNickname());
        return new ResponseEntity<>(new MemberResponse(result.result.getUsername()), HttpStatus.CREATED);
    }

    @PutMapping(value = "/authenticate")
    public ResponseEntity<Boolean> authentication(@RequestBody MemberAuthenticateRequest request) {
        String[] decrypt = aes256Util.decrypt(request.getToken()).split("이메일인증코드번호:");
        if(decrypt.length != 2)throw new NotMatchedCryptException("잘못된 토큰 값입니다.");
        return ResponseEntity.ok(this.memberService.authenticate(decrypt[1]));
    }

    @GetMapping(value = "/is/usernames/{username}")
    public ResponseEntity<Boolean> isUsername(@PathVariable String username) {
        return ResponseEntity.ok(this.memberService.isMemberByUsername(username));
    }

    @GetMapping(value = "/is/nicknames/{nickname}")
    public ResponseEntity<Boolean> isNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(this.memberService.isMemberByNickname(nickname));
    }

    @GetMapping(value = "/profile/me")
    public ResponseEntity<MemberProfileResponse> profile(Principal principal) {
        Member member = this.memberService.findMember(principal.getName());
        return ResponseEntity.ok(new MemberProfileResponse(member.getUsername(), member.getNickname(), member.getPermission(), member.getCreatedDate()));
    }

    @PutMapping(value = "/profile/me")
    public ResponseEntity<MemberResponse> update(Principal principal, @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(new MemberResponse(this.memberService.changeNickname(principal.getName(), request.getNickname())));
    }
}
