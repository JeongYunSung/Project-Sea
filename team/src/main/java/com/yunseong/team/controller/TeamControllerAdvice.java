package com.yunseong.team.controller;

import com.yunseong.common.NotMatchedCryptException;
import com.yunseong.common.UnsupportedStateTransitionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TeamControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("notFound", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotMatchedCryptException.class)
    public ResponseEntity handleNotMatchCryptException(NotMatchedCryptException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("notMatchedToken", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UnsupportedStateTransitionException.class)
    public ResponseEntity handleUnsupportedStateTransitionException(UnsupportedStateTransitionException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("unsupportedState", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
