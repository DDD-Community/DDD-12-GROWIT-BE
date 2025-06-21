package com.growit.app.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.user.controller.dto.request.TokenResponse;
import com.growit.app.user.controller.mapper.RequestMapper;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.usecase.ReissueUseCase;
import com.growit.app.user.usecase.SignInUseCase;
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

  @MockBean private SignUpUseCase signUpUseCase;
  @MockBean private SignInUseCase signInUseCase;
  @MockBean private ReissueUseCase reissueUseCase;
  @MockBean private RequestMapper requestMapper;
  @MockBean private ResponseMapper responseMapper;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
  }

  @Test
  void signupTest() throws Exception {
    String requestBody =
        """
            {
                "email": "test@example.com",
                "password": "securePass123",
                "name": "홍길동",
                "jobRoleId": "6rOg7Zmp7IOd",
                "careerYear": "JUNIOR"
            }
            """;
    // mock 동작 정의 (필요 시)
    mockMvc
        .perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andDo(
            MockMvcRestDocumentationWrapper.document(
                "auth-signup",
                new ResourceSnippetParametersBuilder()
                    .tag("Auth")
                    .description("사용자 회원가입")
                    .requestFields(
                        fieldWithPath("email").type(STRING).description("사용자 이메일"),
                        fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                        fieldWithPath("jobRoleId").type(STRING).description("직무 ID"),
                        fieldWithPath("careerYear")
                            .type(STRING)
                            .description("경력 연차 (예: JUNIOR, MID, SENIOR)"))));
  }

  @Test
  void signInTest() throws Exception {
    String requestBody =
        """
            {
                "email": "test@example.com",
                "password": "securePass123"
            }
            """;
    Token token = new Token("accessToken", "refreshToken");
    given(responseMapper.toTokenResponse(any()))
        .willReturn(new TokenResponse(token.accessToken(), token.refreshToken()));
    mockMvc
        .perform(post("/auth/signin").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andDo(
            MockMvcRestDocumentationWrapper.document(
                "auth-signin",
                new ResourceSnippetParametersBuilder()
                    .tag("Auth")
                    .description("사용자 로그인")
                    .requestFields(
                        fieldWithPath("email").type(STRING).description("사용자 이메일"),
                        fieldWithPath("password").type(STRING).description("사용자 비밀번호"))
                    .responseFields(
                        fieldWithPath("data.accessToken").type(STRING).description("엑세스 토큰"),
                        fieldWithPath("data.refreshToken").type(STRING).description("리프레시 토큰"))));
  }

  @Test
  void reissueTest() throws Exception {
    String requestBody =
        """
            {
                "refreshToken": "dummy-refresh-token"
            }
            """;
    Token token = new Token("accessToken", "refreshToken");
    given(responseMapper.toTokenResponse(any()))
        .willReturn(new TokenResponse(token.accessToken(), token.refreshToken()));

    mockMvc
        .perform(post("/auth/reissue").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andDo(
            MockMvcRestDocumentationWrapper.document(
                "auth-reissue",
                new ResourceSnippetParametersBuilder()
                    .tag("Auth")
                    .description("토큰 재발급")
                    .requestFields(
                        fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰"))
                    .responseFields(
                        fieldWithPath("data.accessToken").type(STRING).description("엑세스 토큰"),
                        fieldWithPath("data.refreshToken").type(STRING).description("리프레시 토큰"))));
  }
}
