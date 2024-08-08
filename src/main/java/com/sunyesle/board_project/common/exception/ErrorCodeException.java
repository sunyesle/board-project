package com.sunyesle.board_project.common.exception;

import lombok.Getter;

@Getter
public class ErrorCodeException extends RuntimeException {
    private final ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
