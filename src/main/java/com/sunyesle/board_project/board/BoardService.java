package com.sunyesle.board_project.board;

import com.sunyesle.board_project.common.dto.CreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public CreateResponse saveBoard(BoardRequest request, Long loginMemberId) {
        Board board = new Board(loginMemberId, request.getTitle(), request.getContent());
        boardRepository.save(board);
        return new CreateResponse(board.getId());
    }

    public Page<BoardResponse> getBoards(String title, Pageable pageable) {
        if (title == null || title.trim().length() <= 2) {
            return boardRepository.findByTitleContains(null, pageable);
        }
        return boardRepository.findByTitleContains(title, pageable);
    }
}
