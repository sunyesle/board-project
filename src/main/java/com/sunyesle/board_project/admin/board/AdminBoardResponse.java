package com.sunyesle.board_project.admin.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminBoardResponse {
    private final Long id;
    private final Long memberId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final LocalDateTime deletedAt;

    public AdminBoardResponse(Long id, Long memberId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public static AdminBoardResponse of(AdminBoardEntity entity) {
        return new AdminBoardResponse(
                entity.getId(),
                entity.getMemberId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt()
        );
    }
}
