package com.yunseong.member.controller;

import com.yunseong.common.AlreadyExistedEntityException;
import com.yunseong.common.NotMatchedCryptException;
import com.yunseong.member.service.AlreadyAuthenticatedUsernameException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(AlreadyAuthenticatedUsernameException.class)
    public ResponseEntity<?> handleAlreadyAuthenticatedUsernameException(AlreadyAuthenticatedUsernameException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("already authenticated", "이미 인증된 계정입니다.");
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(NotMatchedCryptException.class)
    public ResponseEntity<?> handleNotMatchCryptException(NotMatchedCryptException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("not Matched Token", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("entity not found", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("username", "해당 계정의 소유자는 존재하지 않습니다.");
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(AlreadyExistedEntityException.class)
    public ResponseEntity<?> handleAlreadyExistedEntityException(AlreadyExistedEntityException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", "해당 계정 및 닉네임은 이미 존재합니다.");
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }
}
