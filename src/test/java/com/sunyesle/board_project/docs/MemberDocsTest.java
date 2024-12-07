package com.sunyesle.board_project.docs;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.docs.support.BaseRestDocsTest;
import com.sunyesle.board_project.member.MemberController;
import com.sunyesle.board_project.member.MemberService;
import com.sunyesle.board_project.member.dto.MemberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberDocsTest extends BaseRestDocsTest {
    private static final String TAG = "Member API";

    @MockBean
    private MemberService memberService;

    @Test
    void signupTest() throws Exception {
        MemberRequest memberRequest = new MemberRequest("test@gamil.com", "테스트", "010-0000-0000", "Test12345!@");
        CreateResponse response = new CreateResponse(1L);
        given(memberService.signup(any(MemberRequest.class)))
                .willReturn(response);

        FieldDescriptor[] requestFields = {
                fieldWithPath("email").description("이메일"),
                fieldWithPath("name").description("이름"),
                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                fieldWithPath("password").description("비밀번호")
        };
        FieldDescriptor[] responseFields = {
                fieldWithPath("id").description("회원 id")
        };

        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isCreated())
                // REST Docs
                .andDo(MockMvcRestDocumentation.document("signup",
                        requestFields(requestFields),
                        responseFields(responseFields)))
                // OAS 3.0 - Swagger
                .andDo(MockMvcRestDocumentationWrapper.document("signup",
                        resource(ResourceSnippetParameters.builder()
                                .tag(TAG)
                                .description("회원가입")
                                .requestFields(requestFields)
                                .responseFields(responseFields)
                                .build())));
    }
}
