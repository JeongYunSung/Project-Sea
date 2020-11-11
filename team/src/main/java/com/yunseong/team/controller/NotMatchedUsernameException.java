package com.yunseong.team.controller;

public class NotMatchedUsernameException extends RuntimeException {

    public NotMatchedUsernameException(String message) {
        super(message);
    }
}
