package com.sunyesle.board_project.admin.board;

import com.sunyesle.board_project.admin.entity.AdminBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
