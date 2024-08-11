package com.sunyesle.board_project.admin.board;

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
}
