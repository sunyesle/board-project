package com.sunyesle.board_project.admin.board;

import com.sunyesle.board_project.common.exception.BoardErrorCode;
import com.sunyesle.board_project.common.exception.ErrorCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AdminBoardService {
    private final AdminBoardRepository adminBoardRepository;

    public List<AdminBoardResponse> getBoards() {
        List<AdminBoardEntity> boards = adminBoardRepository.findAll();
        return boards.stream().map(AdminBoardResponse::of).toList();
    }

    @Transactional
    public void updateBoard(Long id, AdminBoardRequest request) {
        AdminBoardEntity board = adminBoardRepository.findById(id)
                .orElseThrow(() -> new ErrorCodeException(BoardErrorCode.BOARD_NOT_FOUND));

        board.update(request);
    }
}
