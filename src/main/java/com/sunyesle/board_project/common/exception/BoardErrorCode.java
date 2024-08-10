package com.sunyesle.board_project.common.exception;

import org.springframework.http.HttpStatus;

public enum BoardErrorCode implements ErrorCode {
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 존재하지 않습니다."),
    NOT_BOARD_OWNER(HttpStatus.FORBIDDEN, "게시글 접근 권한이 없습니다."),
    BOARD_MODIFICATION_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST, "게시글 수정 가능 기간이 지났습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    BoardErrorCode(HttpStatus httpStatus, String message) {
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
