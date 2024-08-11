package com.sunyesle.board_project.board;

import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.common.exception.BoardErrorCode;
import com.sunyesle.board_project.common.exception.ErrorCodeException;
import com.sunyesle.board_project.common.exception.MemberErrorCode;
import com.sunyesle.board_project.member.Member;
import com.sunyesle.board_project.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final Clock clock;

    @Transactional
    public CreateResponse saveBoard(BoardRequest request, Long loginMemberId) {
        Board board = new Board(loginMemberId, request.getTitle(), request.getContent());
        boardRepository.save(board);
        return new CreateResponse(board.getId());
    }

    public BoardResponse getBoard(Long id) {
        Board board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ErrorCodeException(BoardErrorCode.BOARD_NOT_FOUND));

        Member writer = memberRepository.findById(board.getMemberId())
                .orElseThrow(() -> new ErrorCodeException(MemberErrorCode.MEMBER_NOT_FOUND));

        return BoardResponse.of(board, writer);
    }

    public Page<BoardResponse> getBoards(String title, Pageable pageable) {
        if (title == null || title.trim().length() <= 2) {
            return boardRepository.findByTitleContains(null, pageable);
        }
        return boardRepository.findByTitleContains(title, pageable);
    }

    @Transactional
    public void deleteBoard(Long id, Long loginMemberId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ErrorCodeException(BoardErrorCode.BOARD_NOT_FOUND));

        // 로그인한 회원이 게시글의 작성자가 아니면 예외를 던진다.
        if (!board.getMemberId().equals(loginMemberId)) {
            throw new ErrorCodeException(BoardErrorCode.NOT_BOARD_OWNER);
        }

        // 이미 삭제된 게시글일 경우
        if (board.getDeletedAt() != null) {
            return;
        }

        board.setDeletedAt(LocalDateTime.now(clock));
    }

    public void updateBoard(Long id, BoardRequest request, Long loginMemberId) {
        Board board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ErrorCodeException(BoardErrorCode.BOARD_NOT_FOUND));

        // 로그인한 회원이 게시글의 작성자가 아니면 예외를 던진다.
        if (!board.getMemberId().equals(loginMemberId)) {
            throw new ErrorCodeException(BoardErrorCode.NOT_BOARD_OWNER);
        }

        // 게시글 작성 후 10일이 지난 경우 예외를 던진다.
        if (LocalDateTime.now(clock).isAfter(board.getCreatedAt().plusDays(10L))) {
            throw new ErrorCodeException(BoardErrorCode.BOARD_MODIFICATION_PERIOD_EXPIRED);
        }

        board.update(request);
    }
}
