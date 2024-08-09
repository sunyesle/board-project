package com.sunyesle.board_project.board;

import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.common.security.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<CreateResponse> saveBoard(@LoginMember Long loginMemberId, @Valid @RequestBody BoardRequest request){
        CreateResponse response = boardService.saveBoard(request, loginMemberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
