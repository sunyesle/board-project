package com.sunyesle.board_project.board;

import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Board extends BaseEntity {
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

    public void delete(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void update(BoardRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
