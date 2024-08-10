package com.sunyesle.board_project.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final MemberDto writer;

    public BoardResponse(Long id, String title, String content, LocalDateTime createdAt, long writerId, String writerName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.writer = new MemberDto(writerId, writerName);
    }

    private record MemberDto(Long id, String name) {
    }
}
