package com.sunyesle.board_project.board;

import lombok.Getter;

@Getter
public class BoardRequest {
    private final String title;
    private final String content;

    public BoardRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}