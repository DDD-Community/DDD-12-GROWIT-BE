package com.growit.app.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.usecase.SignUpUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class AuthControllerTest {

  private MockMvc mockMvc;

  @MockBean private SignUpUseCase signUpUseCase; // ✅ mock bean 주입

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
  }

  @Test
  void postSignUpTest() throws Exception {
    String requestBody =
        """
        {
            "email": "test@example.com",
            "password": "securePass123",
            "name": "홍길동",
            "jobRoleId": "backend",
            "careerYear": "JUNIOR"
        }
        """;
    // mock 동작 정의 (필요 시)
    doNothing().when(signUpUseCase).execute(any(SignUpCommand.class));

    mockMvc
        .perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andDo(
            MockMvcRestDocumentationWrapper.document(
                "auth-signup",
                new ResourceSnippetParametersBuilder()
                    .tag("Auth")
                    .description("사용자 회원가입 요청")
                    .requestFields(
                        fieldWithPath("email").type(STRING).description("사용자 이메일"),
                        fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                        fieldWithPath("jobRoleId").type(STRING).description("직무 ID"),
                        fieldWithPath("careerYear")
                            .type(STRING)
                            .description("경력 연차 (예: JUNIOR, MID, SENIOR)"))));
  }
}
