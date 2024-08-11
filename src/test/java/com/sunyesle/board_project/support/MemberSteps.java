package com.sunyesle.board_project.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.common.security.LoginRequest;
import com.sunyesle.board_project.member.dto.MemberRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

public class MemberSteps {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static ExtractableResponse<Response> 회원가입_요청(MemberRequest memberRequest) {
        return RestAssured
                .given().log().all()
                    .basePath("/api/v1/members")
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(memberRequest))
                .when()
                    .post()
                .then().log().all()
                    .extract();
    }

    @SneakyThrows
    public static ExtractableResponse<Response> 로그인_요청(LoginRequest loginRequest) {
        return RestAssured
                .given().log().all()
                    .basePath("/api/v1/members/login")
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(loginRequest))
                .when()
                    .post()
                .then().log().all()
                    .extract();
    }
}
