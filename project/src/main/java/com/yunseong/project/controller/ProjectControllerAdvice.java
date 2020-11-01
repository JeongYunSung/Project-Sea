package com.yunseong.project.controller;

import com.yunseong.common.AlreadyExistedElementException;
import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.service.CannotReadBecausePrivateProjectException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ProjectControllerAdvice {

    @ExceptionHandler(AlreadyExistedElementException.class)
    public ResponseEntity<?> handleAlreadyExistedElementException(AlreadyExistedElementException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.rejectValue("username", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(CannotReadBecausePrivateProjectException.class)
    public ResponseEntity<?> handleCannotReadBecausePrivateProjectException(CannotReadBecausePrivateProjectException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", exception.getMessage());
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

    @ExceptionHandler(UnsupportedStateTransitionException.class)
    public ResponseEntity<?> handleUnsupportedStateTransitionException(UnsupportedStateTransitionException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("unsupportedState", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }
}
