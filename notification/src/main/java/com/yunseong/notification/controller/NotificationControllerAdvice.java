package com.yunseong.notification.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificationControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
