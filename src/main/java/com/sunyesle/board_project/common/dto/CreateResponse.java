package com.sunyesle.board_project.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class CreateResponse {
    private final Long id;

    @JsonCreator
    public CreateResponse(Long id) {
        this.id = id;
    }
}
