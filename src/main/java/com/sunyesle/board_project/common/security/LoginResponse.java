package com.sunyesle.board_project.common.security;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final Long memberId;
    private final String username;

    public LoginResponse(Long memberId, String username) {
        this.memberId = memberId;
        this.username = username;
    }
}
