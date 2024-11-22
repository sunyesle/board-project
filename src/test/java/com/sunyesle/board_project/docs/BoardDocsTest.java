package com.sunyesle.board_project.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.board.BoardController;
import com.sunyesle.board_project.board.BoardService;
import com.sunyesle.board_project.board.dto.BoardDetailResponse;
import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.docs.support.WithCustomMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
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
                        responseFields(fieldWithPath("id").description("게시글 id"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("createdAt").description("게시글 생성일시"),
                                fieldWithPath("modificationDeadline").description("게시글 수정가능일시"),
                                fieldWithPath("writer.id").description("게시글 작성자 아이디"),
                                fieldWithPath("writer.name").description("게시글 작성자 이름")
                        )
                ));
    }
}
