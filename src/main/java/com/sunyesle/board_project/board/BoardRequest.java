package com.sunyesle.board_project.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardRequest {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다")
    private final String title;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    @Size(max = 1000, message = "내용은 1000자 이하여야 합니다")
    private final String content;

    public BoardRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}