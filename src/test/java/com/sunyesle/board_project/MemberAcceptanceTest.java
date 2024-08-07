package com.sunyesle.board_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.common.security.LoginRequest;
import com.sunyesle.board_project.member.Member;
import com.sunyesle.board_project.member.MemberRepository;
import com.sunyesle.board_project.member.MemberRequest;
import com.sunyesle.board_project.support.BaseAcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends BaseAcceptanceTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Override
    public void setUp() {
        super.setUp();
        memberRepository.deleteAll();
    }

    @DisplayName("회원가입을 한다")
    @Test
    void signup() {
        // given
        String email = "test@gamil.com";
        String name = "테스트";
        String phoneNumber = "010-0000-0000";
        String password = "Test1234!@";
        MemberRequest memberRequest = new MemberRequest(email, name, phoneNumber, password);

        // when
        ExtractableResponse<Response> response = 회원가입_요청(memberRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long id = response.as(CreateResponse.class).getId();
        Optional<Member> savedMember = memberRepository.findById(id);
        assertThat(savedMember).isPresent();
    }

    @SneakyThrows
    private ExtractableResponse<Response> 회원가입_요청(MemberRequest memberRequest) {
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
    @DisplayName("로그인을 한다")
    @Test
    void login() {
        // given
        String email = "test@gamil.com";
        String name = "테스트";
        String phoneNumber = "010-0000-0000";
        String password = "Test1234!@";
        회원가입_요청(new MemberRequest(email, name, phoneNumber, password));

        LoginRequest loginRequest = new LoginRequest(email, password);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .basePath("/api/v1/members/login")
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(loginRequest))
                .when()
                    .post()
                .then().log().all()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.header("access_token")).isNotBlank();
    }
}
