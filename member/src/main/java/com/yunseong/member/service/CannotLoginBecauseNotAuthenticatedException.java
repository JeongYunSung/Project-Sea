package com.yunseong.member.service;

public class CannotLoginBecauseNotAuthenticatedException extends RuntimeException {

    public CannotLoginBecauseNotAuthenticatedException(String message) {
        super(message);
    }
}
