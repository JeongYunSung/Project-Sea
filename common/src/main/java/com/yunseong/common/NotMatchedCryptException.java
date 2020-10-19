package com.yunseong.common;

public class NotMatchedCryptException extends RuntimeException {

    public NotMatchedCryptException(String message) {
        super(message);
    }
}
