package com.sunyesle.board_project.admin.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/boards")
@RequiredArgsConstructor
public class AdminBoardController {
    private final AdminBoardService adminBoardService;

    @GetMapping
    public ResponseEntity<List<AdminBoardResponse>> getBoard() {
        List<AdminBoardResponse> boards = adminBoardService.getBoards();
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }
}
