package com.sunyesle.board_project.admin.board;

import com.sunyesle.board_project.common.security.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(
            @PathVariable Long id,
            @Valid @RequestBody AdminBoardRequest request
    ) {
        adminBoardService.updateBoard(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        adminBoardService.deleteBoard(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
