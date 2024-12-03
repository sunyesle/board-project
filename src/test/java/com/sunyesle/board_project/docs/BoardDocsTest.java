package com.sunyesle.board_project.docs;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.sunyesle.board_project.board.BoardController;
import com.sunyesle.board_project.board.BoardService;
import com.sunyesle.board_project.board.dto.BoardDetailResponse;
import com.sunyesle.board_project.board.dto.BoardRequest;
import com.sunyesle.board_project.board.dto.BoardResponse;
import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.docs.support.BaseRestDocsTest;
import com.sunyesle.board_project.docs.support.WithCustomMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardDocsTest extends BaseRestDocsTest {
    private static final String TAG = "Board API";

    @MockBean
    private BoardService boardService;

    @WithCustomMockUser
    @Test
    void saveBoardTest() throws Exception {
        BoardRequest boardRequest = new BoardRequest("제목", "내용");
        CreateResponse response = new CreateResponse(1L);
        given(boardService.saveBoard(any(BoardRequest.class), notNull()))
                .willReturn(response);

        FieldDescriptor[] requestFields = {
                fieldWithPath("title").description("제목"),
                fieldWithPath("content").description("내용")
        };
        FieldDescriptor[] responseFields = {
                fieldWithPath("id").description("게시글 id")
        };

        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(objectMapper.writeValueAsString(boardRequest)))
                .andExpect(status().isCreated())
                // REST Docs
                .andDo(MockMvcRestDocumentation.document("save-board",
                        requestFields(requestFields),
                        responseFields(responseFields)))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("save-board",
                        resource(ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .description("게시글 작성")
                                .requestFields(requestFields)
                                .responseFields(responseFields)
                                .build())));
    }

    @Test
    void getBoardTest() throws Exception {
        BoardDetailResponse response = new BoardDetailResponse(1L, "제목", "내용", LocalDateTime.of(2024, 11, 10, 10, 0), LocalDateTime.of(2024, 11, 20, 10, 0), 1L, "작성자 이름");
        given(boardService.getBoard(any()))
                .willReturn(response);

        ParameterDescriptor[] pathParameters = {
                parameterWithName("id").description("게시글 id")
        };
        FieldDescriptor[] responseFields = {
                fieldWithPath("id").description("게시글 id"),
                fieldWithPath("title").description("게시글 제목"),
                fieldWithPath("content").description("게시글 내용"),
                fieldWithPath("createdAt").description("게시글 생성일시"),
                fieldWithPath("modificationDeadline").description("게시글 수정가능일시"),
                fieldWithPath("writer").description("작성자 정보"),
                fieldWithPath("writer.id").description("작성자 id"),
                fieldWithPath("writer.name").description("작성자 이름")
        };

        mockMvc.perform(get("/api/v1/boards/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // REST Docs
                .andDo(MockMvcRestDocumentation.document("get-board",
                        pathParameters(pathParameters),
                        responseFields(responseFields)))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("get-board",
                        resource(ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .description("게시글 상세 조회")
                                .pathParameters(pathParameters)
                                .responseFields(responseFields)
                                .build())));
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

        ParameterDescriptor[] queryParameters = {
                parameterWithName("title").description("제목 검색 키워드").optional(),
                parameterWithName("pageNumber").description("페이지").optional(),
                parameterWithName("pageSize").description("페이지 사이즈").optional(),
                parameterWithName("orderBy").description("정렬").optional()
        };
        FieldDescriptor[] responseFields = {
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
        };

        mockMvc.perform(get("/api/v1/boards")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("title", "Title")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("orderBy", "LATEST"))
                .andExpect(status().isOk())
                // REST Docs
                .andDo(MockMvcRestDocumentation.document("get-boards",
                        queryParameters(queryParameters),
                        responseFields(responseFields)))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("get-boards",
                        resource(ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .description("게시글 목록 조회")
                                .queryParameters(queryParameters)
                                .responseFields(responseFields)
                                .build())));
    }

    @Test
    @WithCustomMockUser
    void updateBoardTest() throws Exception {
        BoardRequest request = new BoardRequest("제목", "내용");

        ParameterDescriptor[] pathParameters = {
                parameterWithName("id").description("게시글 id")
        };
        FieldDescriptor[] requestFields = {
                fieldWithPath("title").description("제목"),
                fieldWithPath("content").description("내용")
        };

        mockMvc.perform(put("/api/v1/boards/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                // REST Docs
                .andDo(MockMvcRestDocumentation.document("update-board",
                        pathParameters(pathParameters),
                        requestFields(requestFields)))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("update-board",
                        resource(ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .description("게시글 수정")
                                .pathParameters(pathParameters)
                                .requestFields(requestFields)
                                .build())));
    }

    @Test
    @WithCustomMockUser
    void deleteBoardTest() throws Exception {
        ParameterDescriptor[] pathParameters = {
                parameterWithName("id").description("게시글 id")
        };

        mockMvc.perform(delete("/api/v1/boards/{id}", 1L)
                        .header("Authorization", "Bearer {ACCESS_TOKEN}"))
                .andExpect(status().isNoContent())
                // REST Docs
                .andDo(MockMvcRestDocumentation.document("delete-board",
                        pathParameters(pathParameters)))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("delete-board",
                        resource(ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .description("게시글 삭제")
                                .pathParameters(pathParameters)
                                .build())));
    }
}
