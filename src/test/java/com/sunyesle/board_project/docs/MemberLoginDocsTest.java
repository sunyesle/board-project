package com.sunyesle.board_project.docs;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.board_project.common.security.LoginRequest;
import com.sunyesle.board_project.member.Member;
import com.sunyesle.board_project.member.MemberRepository;
import com.sunyesle.board_project.member.dto.MemberRole;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("restdocs")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class MemberLoginDocsTest {
    private static final String TAG = "Member API";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private MemberRepository memberRepository;

    @Test
    void loginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@gamil.com", "Test12345!@");
        Member member = new Member(1L, "test@gamil.com", "$2a$10$sNRdglkjDTBb3mK4zU30UORw8wJyKV75GY8rK4Fi7tX3Ib0zJfFJG", "테스트", "010-0000-0000", MemberRole.USER);
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(member));

        FieldDescriptor[] requestFields = {
                fieldWithPath("username").description("이메일"),
                fieldWithPath("password").description("비밀번호")
        };
        HeaderDescriptor[] responseHeaders = {
                headerWithName("access_token").description("ACCESS_TOKEN")
        };
        FieldDescriptor[] responseFields = {
                fieldWithPath("memberId").description("회원 id"),
                fieldWithPath("username").description("이메일")
        };

        mockMvc.perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                //REST Docs
                .andDo(MockMvcRestDocumentation.document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(requestFields),
                        responseHeaders(responseHeaders),
                        responseFields(responseFields)
                ))
                //OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(TAG)
                                        .description("로그인")
                                        .requestFields(requestFields)
                                        .responseHeaders(responseHeaders)
                                        .responseFields(responseFields)
                                        .build())));
    }
}
