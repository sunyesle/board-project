package com.sunyesle.board_project.common.exception;

import org.springframework.http.HttpStatus;

public enum FileErrorCode implements ErrorCode {
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "올바르지 않은 파일명입니다."),
    FILE_STORE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    FileErrorCode(HttpStatus httpStatus, String message) {
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
