package com.yunseong.board.service;

public class CannotRecommendByWriterException extends RuntimeException {

    public CannotRecommendByWriterException(String message) {
        super(message);
    }
}
