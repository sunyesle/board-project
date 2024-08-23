package com.sunyesle.board_project;

import com.sunyesle.board_project.board.Board;
import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.common.exception.BoardErrorCode;
import com.sunyesle.board_project.common.exception.ErrorCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardTest {

    @DisplayName("작성 후 10일이 지난 게시글은 수정할 수 없다.")
    @Test
    void updateBoardTest() {
        // given
        LocalDateTime createdAt = LocalDateTime.parse("2024-01-01T00:00:00");
        LocalDateTime now = LocalDateTime.parse("2024-01-11T00:00:01");

        Board board = new Board(1L, 1L, "title", "content", createdAt, createdAt, null);

        BoardRequest request = new BoardRequest("update title", "update content");

        // When
        // Then
        assertThatThrownBy(() -> board.update(request, now))
                .isInstanceOf(ErrorCodeException.class)
                .hasMessage(BoardErrorCode.BOARD_MODIFICATION_PERIOD_EXPIRED.getMessage());
    }
}
