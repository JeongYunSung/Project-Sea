package com.yunseong.notification.service;

public class NotMatchedUsernameException extends RuntimeException {

    public NotMatchedUsernameException(String message) {
        super(message);
    }
}
