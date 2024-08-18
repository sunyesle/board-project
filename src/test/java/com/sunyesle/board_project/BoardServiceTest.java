package com.sunyesle.board_project;

import com.sunyesle.board_project.board.Board;
import com.sunyesle.board_project.board.BoardRepository;
import com.sunyesle.board_project.board.BoardService;
import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.common.exception.BoardErrorCode;
import com.sunyesle.board_project.common.exception.ErrorCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private BoardService boardService;

    @DisplayName("게시글 작성 후 10일이 지나면 수정할 수 없다.")
    @Test
    void updateBoardTest() {
        // given
        LocalDateTime now = LocalDateTime.parse("2024-01-01T00:00:01");
        LocalDateTime modificationDeadline = LocalDateTime.parse("2024-01-01T00:00:00");

        Long boardId = 1L;
        Long loginMemberId = 1L;
        BoardRequest request = new BoardRequest("테스트 게시글", "테스트 내용");

        Board board = mock(Board.class);
        given(board.getMemberId())
                .willReturn(loginMemberId);
        given(board.getModificationDeadline())
                .willReturn(modificationDeadline);

        given(boardRepository.findByIdAndDeletedAtIsNull(boardId))
                .willReturn(Optional.of(board));

        given(clock.instant())
                .willReturn(now.atZone(ZoneId.systemDefault()).toInstant());
        given(clock.getZone())
                .willReturn(ZoneId.systemDefault());

        // When
        // Then
        assertThatThrownBy(() -> boardService.updateBoard(boardId, request, loginMemberId))
                .isInstanceOf(ErrorCodeException.class)
                .hasMessage(BoardErrorCode.BOARD_MODIFICATION_PERIOD_EXPIRED.getMessage());
    }
}
