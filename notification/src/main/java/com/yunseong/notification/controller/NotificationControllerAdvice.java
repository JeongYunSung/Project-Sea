package com.yunseong.notification.controller;

import com.yunseong.notification.service.NotMatchedUsernameException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class NotificationControllerAdvice {

    @ExceptionHandler(NotMatchedUsernameException.class)
    public ResponseEntity<?> handleNotMatchedUsernameException(NotMatchedUsernameException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("not matched", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", "해당 알림은 이미 존재합니다.");
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("notFound", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }
}
