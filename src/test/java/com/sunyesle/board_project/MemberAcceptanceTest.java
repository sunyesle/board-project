package com.sunyesle.board_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.common.security.LoginRequest;
import com.sunyesle.board_project.member.Member;
import com.sunyesle.board_project.member.MemberRepository;
import com.sunyesle.board_project.member.dto.MemberRequest;
import com.sunyesle.board_project.support.BaseAcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.sunyesle.board_project.support.MemberSteps.로그인_요청;
import static com.sunyesle.board_project.support.MemberSteps.회원가입_요청;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends BaseAcceptanceTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
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
        String password = "Test12345!@";
        MemberRequest memberRequest = new MemberRequest(email, name, phoneNumber, password);

        // when
        ExtractableResponse<Response> response = 회원가입_요청(memberRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long id = response.as(CreateResponse.class).getId();
        Optional<Member> savedMember = memberRepository.findById(id);
        assertThat(savedMember).isPresent();
    }

    @DisplayName("중복된 이메일로 회원가입 요청을하면 409를 반환한다")
    @Test
    void signupEmailDuplicate() {
        // given
        String email = "test@gamil.com";
        String name = "테스트";
        String phoneNumber = "010-0000-0000";
        String password = "Test12345!@";
        MemberRequest memberRequest = new MemberRequest(email, name, phoneNumber, password);
        회원가입_요청(memberRequest);

        // when
        ExtractableResponse<Response> response = 회원가입_요청(memberRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("로그인을 한다")
    @Test
    void login() {
        // given
        String email = "test@gamil.com";
        String name = "테스트";
        String phoneNumber = "010-0000-0000";
        String password = "Test12345!@";
        회원가입_요청(new MemberRequest(email, name, phoneNumber, password));

        LoginRequest loginRequest = new LoginRequest(email, password);

        // when
        ExtractableResponse<Response> response = 로그인_요청(loginRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.header("access_token")).isNotBlank();
    }
}
