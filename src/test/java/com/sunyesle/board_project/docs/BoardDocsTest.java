package com.sunyesle.board_project.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.board.BoardController;
import com.sunyesle.board_project.board.BoardService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
}
