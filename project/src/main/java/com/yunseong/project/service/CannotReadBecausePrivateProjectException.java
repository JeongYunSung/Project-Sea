package com.yunseong.project.service;

public class CannotReadBecausePrivateProjectException extends RuntimeException {

    public CannotReadBecausePrivateProjectException(String message) {
        super(message);
    }
}
