package com.yunseong.common;

public class AlreadyExistedEntityException extends RuntimeException {

    public AlreadyExistedEntityException(String message) {
        super(message);
    }
}
