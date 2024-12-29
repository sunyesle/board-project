package com.sunyesle.board_project.acceptance.support;

import com.sunyesle.board_project.board.dto.BoardRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;

public class BoardSteps {

    @SneakyThrows
    public static ExtractableResponse<Response> 게시글_작성_요청(BoardRequest boardRequest, String token) {
        return RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(boardRequest)
                .when()
                    .post("/api/v1/boards")
                .then()
                    .extract();
    }

    @SneakyThrows
     public static ExtractableResponse<Response> 게시글_조회_요청(Long savedBoardId) {
        return RestAssured
                .when()
                    .get("/api/v1/boards/{boardId}", savedBoardId)
                .then()
                    .extract();
    }
}
