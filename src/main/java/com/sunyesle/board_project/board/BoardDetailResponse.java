package com.sunyesle.board_project.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiableUntil;
    private final MemberDto writer;

    public BoardDetailResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiableUntil, long writerId, String writerName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiableUntil = modifiableUntil;
        this.writer = new MemberDto(writerId, writerName);
    }

    private record MemberDto(Long id, String name) {
    }
}
