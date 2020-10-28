package com.yunseong.board.controller;

import com.yunseong.board.service.AlreadyRecommendedException;
import com.yunseong.board.service.CannotRecommendByWriterException;
import com.yunseong.board.service.CannotReviseBoardIfWriterNotWereException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class BoardControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleUsernameNotFoundException(UsernameNotFoundException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.rejectValue("username", "해당 계정의 소유자는 존재하지 않습니다.");
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("entity not found", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(AlreadyRecommendedException.class)
    public ResponseEntity handleAlreadyRecommendedException(AlreadyRecommendedException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("already recommended", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(CannotRecommendByWriterException.class)
    public ResponseEntity handleCannotRecommendByWriterException(CannotRecommendByWriterException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("cannot recommend by writer", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }

    @ExceptionHandler(CannotReviseBoardIfWriterNotWereException.class)
    public ResponseEntity handleCannotReviseBoardIfWriterNotWereException(CannotReviseBoardIfWriterNotWereException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("cannot revise board if writer not were", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.getAllErrors());
    }
}
