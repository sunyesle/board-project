package com.sunyesle.board_project.board;

import com.sunyesle.board_project.board.dto.BoardDetailResponse;
import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.board.dto.BoardResponse;
import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.common.exception.BoardErrorCode;
import com.sunyesle.board_project.common.exception.ErrorCodeException;
import com.sunyesle.board_project.common.exception.MemberErrorCode;
import com.sunyesle.board_project.member.Member;
import com.sunyesle.board_project.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${board.modification.period.days}")
    private int modificationPeriodDays;

    @Transactional
    public CreateResponse saveBoard(BoardRequest request, Long loginMemberId) {
        Board board = new Board(loginMemberId, request.getTitle(), request.getContent());
        boardRepository.save(board);
        return new CreateResponse(board.getId());
    }

    public BoardDetailResponse getBoard(Long id) {
        Board board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ErrorCodeException(BoardErrorCode.BOARD_NOT_FOUND));

        Member writer = memberRepository.findById(board.getMemberId())
                .orElseThrow(() -> new ErrorCodeException(MemberErrorCode.MEMBER_NOT_FOUND));

        return new BoardDetailResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedAt(),
                board.getCreatedAt().plusDays(modificationPeriodDays),
                writer.getId(),
                writer.getName()
        );
    }

    public Page<BoardResponse> getBoards(String title, Pageable pageable) {
        if (title == null || title.trim().length() <= 2) {
            return boardRepository.findByTitleContains(null, pageable);
        }
        return boardRepository.findByTitleContains(title, pageable);
    }

    @Transactional
    public void deleteBoard(Long id, Long loginMemberId) {
        Board board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ErrorCodeException(BoardErrorCode.BOARD_NOT_FOUND));

        // 로그인한 회원이 게시글의 작성자가 아니면 예외를 던진다.
        if (!board.getMemberId().equals(loginMemberId)) {
            throw new ErrorCodeException(BoardErrorCode.NOT_BOARD_OWNER);
        }

        board.delete(LocalDateTime.now(clock));
    }

    @Transactional
    public void updateBoard(Long id, BoardRequest request, Long loginMemberId) {
        Board board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ErrorCodeException(BoardErrorCode.BOARD_NOT_FOUND));

        // 로그인한 회원이 게시글의 작성자가 아니면 예외를 던진다.
        if (!board.getMemberId().equals(loginMemberId)) {
            throw new ErrorCodeException(BoardErrorCode.NOT_BOARD_OWNER);
        }

        // 게시글 작성 후 10일이 지난 경우 예외를 던진다.
        if (LocalDateTime.now(clock).isAfter(board.getCreatedAt().plusDays(modificationPeriodDays))) {
            throw new ErrorCodeException(BoardErrorCode.BOARD_MODIFICATION_PERIOD_EXPIRED);
        }

        board.update(request);
    }
}
