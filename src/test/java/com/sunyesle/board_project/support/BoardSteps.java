package com.sunyesle.board_project.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.board.dto.BoardRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;

public class BoardSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static ExtractableResponse<Response> 게시글_작성_요청(BoardRequest boardRequest, String token) {
        return RestAssured
                .given().log().all()
                    .basePath("/api/v1/boards")
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(objectMapper.writeValueAsString(boardRequest))
                .when()
                    .post()
                .then().log().all()
                    .extract();
    }

    @SneakyThrows
     public static ExtractableResponse<Response> 게시글_조회_요청(Long savedBoardId) {
        return RestAssured
                .given().log().all()
                    .basePath("/api/v1/boards/" + savedBoardId)
                    .contentType(ContentType.JSON)
                .when()
                    .get()
                .then().log().all()
                    .extract();
    }
}
