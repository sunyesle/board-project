package com.sunyesle.board_project.board;

import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.common.entity.BaseEntity;
import com.sunyesle.board_project.common.exception.BoardErrorCode;
import com.sunyesle.board_project.common.exception.ErrorCodeException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Board extends BaseEntity {
    private static final int MODIFICATION_PERIOD_DAYS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    private Long memberId;

    private String title;

    private String content;

    protected Board() {
    }

    public Board(Long memberId, String title, String content) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
    }

    public Board(Long id, Long memberId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public void delete(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void update(BoardRequest request, LocalDateTime now) {
        // 게시글 수정 가능기간이 지난 경우 예외를 던진다.
        if (now.isAfter(getModificationDeadline())) {
            throw new ErrorCodeException(BoardErrorCode.BOARD_MODIFICATION_PERIOD_EXPIRED);
        }

        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public LocalDateTime getModificationDeadline() {
        return this.getCreatedAt().plusDays(MODIFICATION_PERIOD_DAYS);
    }
}
