package com.sunyesle.board_project.board.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modificationDeadline;
    private final MemberDto writer;

    public BoardDetailResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime modificationDeadline, long writerId, String writerName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modificationDeadline = modificationDeadline;
        this.writer = new MemberDto(writerId, writerName);
    }

    private record MemberDto(Long id, String name) {
    }
}
