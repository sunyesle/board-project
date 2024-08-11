package com.sunyesle.board_project.admin.board;

import com.sunyesle.board_project.admin.board.dto.AdminBoardRequest;
import com.sunyesle.board_project.admin.entity.AdminBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "BOARD")
@NoArgsConstructor
@AllArgsConstructor
public class AdminBoardEntity extends AdminBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    private Long memberId;

    private String title;

    private String content;

    public void update(AdminBoardRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void setDeletedAt(LocalDateTime now) {
        this.deletedAt = now;
    }
}
