package com.sunyesle.board_project.member;

import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.member.dto.MemberRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CreateResponse> signup(@Valid @RequestBody MemberRequest request){
        CreateResponse response = memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
