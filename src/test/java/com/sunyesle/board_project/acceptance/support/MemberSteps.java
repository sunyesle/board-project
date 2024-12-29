package com.sunyesle.board_project.acceptance.support;

import com.sunyesle.board_project.common.security.LoginRequest;
import com.sunyesle.board_project.member.dto.MemberRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

public class MemberSteps {

    @SneakyThrows
    public static ExtractableResponse<Response> 회원가입_요청(MemberRequest memberRequest) {
        return RestAssured
                .given()
                    .basePath("/api/v1/members")
                    .contentType(ContentType.JSON)
                    .body(memberRequest)
                .when()
                    .post()
                .then()
                    .extract();
    }

    @SneakyThrows
    public static ExtractableResponse<Response> 로그인_요청(LoginRequest loginRequest) {
        return RestAssured
                .given()
                    .basePath("/api/v1/members/login")
                    .contentType(ContentType.JSON)
                    .body(loginRequest)
                .when()
                    .post()
                .then()
                    .extract();
    }
}
