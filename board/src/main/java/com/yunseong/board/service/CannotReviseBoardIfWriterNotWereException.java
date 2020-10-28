package com.yunseong.board.service;

public class CannotReviseBoardIfWriterNotWereException extends RuntimeException {

    public CannotReviseBoardIfWriterNotWereException(String message) {
        super(message);
    }
}
