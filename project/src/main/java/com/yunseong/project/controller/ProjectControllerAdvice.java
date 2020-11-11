package com.yunseong.project.controller;

import com.yunseong.common.CannotReviseBoardIfWriterNotWereException;
import com.yunseong.common.UnsupportedStateTransitionException;
import com.yunseong.project.domain.EmptyCollectionException;
import com.yunseong.project.service.CannotReadBecausePrivateProjectException;
import com.yunseong.project.service.NotReviseForUnsupportedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ProjectControllerAdvice {

    @ExceptionHandler(NotReviseForUnsupportedException.class)
    public ResponseEntity<?> handleNotReviseForUnsupportedException(NotReviseForUnsupportedException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("can not revise", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(CannotReviseBoardIfWriterNotWereException.class)
    public ResponseEntity<?> handleCannotReviseBoardIfWriterNotWereException(CannotReviseBoardIfWriterNotWereException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("can not revise", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(EmptyCollectionException.class)
    public ResponseEntity<?> handleEmptyCollectionException(EmptyCollectionException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("empty collection", exception.getMessage());
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
