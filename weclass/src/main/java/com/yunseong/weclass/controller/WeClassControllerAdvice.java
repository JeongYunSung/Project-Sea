package com.yunseong.weclass.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class WeClassControllerAdvice {

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
}
