package com.sunyesle.board_project.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.board.BoardController;
import com.sunyesle.board_project.board.BoardService;
import com.sunyesle.board_project.board.dto.BoardDetailResponse;
import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.board.dto.BoardResponse;
import com.sunyesle.board_project.common.config.WebConfig;
import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.docs.support.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@Import(WebConfig.class)
class BoardDocsTest {
    @MockBean
    private BoardService boardService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @WithCustomMockUser
    @Test
    void saveBoardTest() throws Exception {
        BoardRequest boardRequest = new BoardRequest("제목", "내용");
        CreateResponse response = new CreateResponse(1L);
        given(boardService.saveBoard(any(BoardRequest.class), notNull()))
                .willReturn(response);

        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(objectMapper.writeValueAsString(boardRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("save-board",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 id")
                        )
                ));
    }

    @Test
    void getBoardTest() throws Exception {
        BoardDetailResponse response = new BoardDetailResponse(1L, "제목", "내용", LocalDateTime.now(), LocalDateTime.now(), 1L, "작성자 이름");
        given(boardService.getBoard(any()))
                .willReturn(response);

        mockMvc.perform(get("/api/v1/boards/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-board",
                        pathParameters(
                                parameterWithName("id").description("게시글 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 id"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("createdAt").description("게시글 생성일시"),
                                fieldWithPath("modificationDeadline").description("게시글 수정가능일시"),
                                fieldWithPath("writer").description("작성자 정보"),
                                fieldWithPath("writer.id").description("작성자 id"),
                                fieldWithPath("writer.name").description("작성자 이름")
                        )
                ));
    }

    @Test
    void getBoardsTest() throws Exception {
        List<BoardResponse> boards = List.of(
                new BoardResponse(1L, "Title 1", "Content 1", LocalDateTime.of(2024, 11, 23, 10, 0), 1L, "Writer A"),
                new BoardResponse(2L, "Title 2", "Content 2", LocalDateTime.of(2024, 11, 24, 12, 30), 2L, "Writer B")
        );
        Page<BoardResponse> response = new PageImpl<>(boards);
        given(boardService.getBoards(any(), any()))
                .willReturn(response);

        mockMvc.perform(get("/api/v1/boards")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "Title")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("orderBy", "LATEST"))

                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-boards",
                        queryParameters(
                                parameterWithName("title").description("제목 검색 키워드"),
                                parameterWithName("pageNumber").description("페이지"),
                                parameterWithName("pageSize").description("페이지 사이즈"),
                                parameterWithName("orderBy").description("정렬")
                        ),
                        responseFields(
                                fieldWithPath("content").description("게시글 리스트"),
                                fieldWithPath("content[].id").description("게시글 id"),
                                fieldWithPath("content[].title").description("게시글 제목"),
                                fieldWithPath("content[].content").description("게시글 내용"),
                                fieldWithPath("content[].createdAt").description("게시글 작성일시"),
                                fieldWithPath("content[].writer").description("작성자 정보"),
                                fieldWithPath("content[].writer.id").description("작성자 id"),
                                fieldWithPath("content[].writer.name").description("작성자 이름"),
                                fieldWithPath("page").description("페이지 정보"),
                                fieldWithPath("page.size").description("페이지 사이즈"),
                                fieldWithPath("page.number").description("현재 페이지 번호"),
                                fieldWithPath("page.totalElements").description("전체 요소 수"),
                                fieldWithPath("page.totalPages").description("전체 페이지 수")
                        )
                ));
    }

    @Test
    @WithCustomMockUser
    void updateBoardTest() throws Exception {
        BoardRequest request = new BoardRequest("제목", "내용");

        mockMvc.perform(put("/api/v1/boards/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("update-board",
                        pathParameters(
                                parameterWithName("id").description("게시글 id")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @WithCustomMockUser
    void deleteBoardTest() throws Exception {
        mockMvc.perform(delete("/api/v1/boards/{id}", 1L)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("delete-board",
                        pathParameters(
                                parameterWithName("id").description("게시글 id")
                        )
                ));
    }
}
