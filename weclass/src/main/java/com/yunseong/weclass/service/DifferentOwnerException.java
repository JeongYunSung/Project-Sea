package com.yunseong.weclass.service;

public class DifferentOwnerException extends RuntimeException {

    public DifferentOwnerException(String message) {
        super(message);
    }
}
