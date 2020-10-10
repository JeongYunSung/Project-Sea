package com.yunseong.member.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleUsernameNotFoundException(UsernameNotFoundException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.rejectValue("username", "해당 계정의 소유자는 존재하지 않습니다.");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
