package com.sunyesle.board_project.board;

import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.common.security.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<CreateResponse> saveBoard(@LoginMember Long loginMemberId, @Valid @RequestBody BoardRequest request) {
        CreateResponse response = boardService.saveBoard(request, loginMemberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id) {
        BoardResponse board = boardService.getBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(board);
    }

    @GetMapping
    public ResponseEntity<Page<BoardResponse>> getBoards(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "LATEST") BoardOrderBy orderBy,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, orderBy.getSort());
        Page<BoardResponse> boards = boardService.getBoards(title, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(
            @LoginMember Long loginMemberId,
            @PathVariable Long id,
            @Valid @RequestBody BoardRequest request
    ) {
        boardService.updateBoard(id, request, loginMemberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@LoginMember Long loginMemberId, @PathVariable Long id) {
        boardService.deleteBoard(id, loginMemberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
