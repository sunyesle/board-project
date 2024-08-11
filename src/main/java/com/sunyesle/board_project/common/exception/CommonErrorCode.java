package com.sunyesle.board_project.common.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
    ACCESS_DENIED(HttpStatus.FORBIDDEN , "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED , "인증 정보가 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CommonErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
