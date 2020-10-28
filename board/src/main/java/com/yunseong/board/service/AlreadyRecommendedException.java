package com.yunseong.board.service;

public class AlreadyRecommendedException extends RuntimeException {

    public AlreadyRecommendedException(String message) {
        super(message);
    }
}
