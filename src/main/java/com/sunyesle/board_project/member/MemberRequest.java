package com.sunyesle.board_project.member;

import lombok.Getter;

@Getter
public class MemberRequest {
    private final String email;
    private final String name;
    private final String phoneNumber;
    private final String password;

    public MemberRequest(String email, String name, String phoneNumber, String password) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
