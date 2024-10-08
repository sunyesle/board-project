package com.sunyesle.board_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.board.*;
import com.sunyesle.board_project.board.dto.BoardOrderBy;
import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.board.dto.BoardResponse;
import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.common.security.LoginRequest;
import com.sunyesle.board_project.member.MemberRepository;
import com.sunyesle.board_project.member.dto.MemberRequest;
import com.sunyesle.board_project.support.BaseAcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static com.sunyesle.board_project.support.BoardSteps.게시글_작성_요청;
import static com.sunyesle.board_project.support.BoardSteps.게시글_조회_요청;
import static com.sunyesle.board_project.support.MemberSteps.로그인_요청;
import static com.sunyesle.board_project.support.MemberSteps.회원가입_요청;
import static org.assertj.core.api.Assertions.assertThat;

class BoardAcceptanceTest extends BaseAcceptanceTest {
    private final String email = "test@gamil.com";
    private final String name = "테스트";
    private final String phoneNumber = "010-0000-0000";
    private final String password = "Test12345!@";

    String accessToken;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        memberRepository.deleteAll();
        boardRepository.deleteAll();

        MemberRequest memberRequest = new MemberRequest(email, name, phoneNumber, password);
        회원가입_요청(memberRequest);
        LoginRequest loginRequest = new LoginRequest(email, password);
        accessToken = 로그인_요청(loginRequest).header("access_token");
    }

    @DisplayName("게시글을 조회한다")
    @Test
    void getBoard() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 내용";
        BoardRequest boardRequest = new BoardRequest(title, content);
        Long savedBoardId = 게시글_작성_요청(boardRequest, accessToken).as(CreateResponse.class).getId();

        // when
        ExtractableResponse<Response> response = 게시글_조회_요청(savedBoardId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("게시글을 작성한다")
    @Test
    void saveBoard() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 내용";
        BoardRequest boardRequest = new BoardRequest(title, content);

        // when
        ExtractableResponse<Response> response = 게시글_작성_요청(boardRequest, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long id = response.as(CreateResponse.class).getId();
        Optional<Board> savedBoard = boardRepository.findById(id);
        assertThat(savedBoard).isPresent();
    }

    @DisplayName("게시글 목록을 조회한다")
    @Test
    void getBoards() {
        // given
        int totalBoards = 30;
        for (int i = 0; i < totalBoards; i++) {
            String title = "테스트 게시글" + i;
            String content = "테스트 내용" + i;
            BoardRequest boardRequest = new BoardRequest(title, content);
            게시글_작성_요청(boardRequest, accessToken);
        }
        int pageSize = 20;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .basePath("/api/v1/boards")
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .queryParam("title", "게시글")
                    .queryParam("orderBy", BoardOrderBy.OLDEST)
                    .queryParam("pageNumber", 0)
                    .queryParam("pageSize", pageSize)
                .when()
                    .get()
                .then().log().all()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        int totalElements = response.jsonPath().getInt("page.totalElements");
        assertThat(totalElements).isEqualTo(totalBoards);

        List<BoardResponse> boards = response.jsonPath().getList("content", BoardResponse.class);
        assertThat(boards).hasSize(pageSize);
    }

    @SneakyThrows
    @DisplayName("인증 없이 게시글을 작성하면 401을 반환한다")
    @Test
    void saveBoardWithoutAuthentication() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 내용";
        BoardRequest boardRequest = new BoardRequest(title, content);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .basePath("/api/v1/boards")
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(boardRequest))
                .when()
                    .post()
                .then().log().all()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @SneakyThrows
    @DisplayName("게시글을 수정한다")
    @Test
    void updateBoard() {
        // given
        BoardRequest boardRequest = new BoardRequest("title", "content");
        Long savedBoardId = 게시글_작성_요청(boardRequest, accessToken).as(CreateResponse.class).getId();

        String title = "테스트 게시글";
        String content = "테스트 내용";
        BoardRequest updateBoardRequest = new BoardRequest(title, content);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .basePath("/api/v1/boards/" + savedBoardId)
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .body(objectMapper.writeValueAsString(updateBoardRequest))
                .when()
                    .put()
                .then().log().all()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("게시글을 삭제한다")
    @Test
    void deleteBoard() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 내용";
        BoardRequest boardRequest = new BoardRequest(title, content);
        Long savedBoardId = 게시글_작성_요청(boardRequest, accessToken).as(CreateResponse.class).getId();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .basePath("/api/v1/boards/" + savedBoardId)
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .when()
                    .delete()
                .then().log().all()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
