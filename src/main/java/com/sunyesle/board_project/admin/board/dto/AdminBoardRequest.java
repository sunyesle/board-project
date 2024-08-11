package com.sunyesle.board_project.admin.board.dto;

import lombok.Getter;

@Getter
public class AdminBoardRequest {
    private final String title;
    private final String content;

    public AdminBoardRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
