package com.sunyesle.board_project;

import com.sunyesle.board_project.board.Board;
import com.sunyesle.board_project.board.BoardRepository;
import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.board.BoardService;
import com.sunyesle.board_project.common.exception.BoardErrorCode;
import com.sunyesle.board_project.common.exception.ErrorCodeException;
import com.sunyesle.board_project.member.MemberRepository;
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
    private MemberRepository memberRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private BoardService boardService;

    @DisplayName("게시글 작성 후 10일이 지나면 수정할 수 없다.")
    @Test
    void updateBoardTest() {
        // given
        LocalDateTime createdAt = LocalDateTime.parse("2024-01-01T00:00:00");
        LocalDateTime now = LocalDateTime.parse("2024-01-11T00:00:01");

        Board board = mock(Board.class);
        given(boardRepository.findByIdAndDeletedAtIsNull(1L))
                .willReturn(Optional.of(board));
        given(board.getMemberId())
                .willReturn(100L);
        given(board.getCreatedAt())
                .willReturn(createdAt);
        given(clock.instant())
                .willReturn(now.atZone(ZoneId.systemDefault()).toInstant());
        given(clock.getZone())
                .willReturn(ZoneId.systemDefault());

        BoardRequest request = new BoardRequest("테스트 게시글", "테스트 내용");

        // When
        // Then
        assertThatThrownBy(() -> boardService.updateBoard(1L, request, 100L))
                .isInstanceOf(ErrorCodeException.class)
                .hasMessage(BoardErrorCode.BOARD_MODIFICATION_PERIOD_EXPIRED.getMessage());
    }
}
