package com.growit.user.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.fake.user.UserFixture;
import com.growit.user.controller.dto.request.SignUpRequest;
import com.growit.user.controller.dto.response.TokenResponse;
import com.growit.user.controller.mapper.RequestMapper;
import com.growit.user.controller.mapper.ResponseMapper;
import com.growit.user.domain.token.Token;
import com.growit.user.usecase.ReissueUseCase;
import com.growit.user.usecase.SignInUseCase;
import com.growit.user.usecase.SignUpUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class AuthControllerTest {
  private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private SignUpUseCase signUpUseCase;
  @MockitoBean private SignInUseCase signInUseCase;
  @MockitoBean private ReissueUseCase reissueUseCase;
  @MockitoBean private RequestMapper requestMapper;
  @MockitoBean private ResponseMapper responseMapper;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
  }

  @Test
  void signupTest() throws Exception {
    SignUpRequest signUpRequest = UserFixture.defaultSignUpRequest();

    mockMvc
        .perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpRequest)))
        .andExpect(status().isCreated())
        .andDo(
            MockMvcRestDocumentationWrapper.document(
                "auth-signup",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Auth")
                        .summary("사용자 회원가입")
                        .requestFields(
                            fieldWithPath("email").type(STRING).description("사용자 이메일"),
                            fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
                            fieldWithPath("name").type(STRING).description("사용자 이름"),
                            fieldWithPath("jobRoleId").type(STRING).description("직무 ID"),
                            fieldWithPath("careerYear")
                                .type(STRING)
                                .description("경력 연차 (예: JUNIOR, MID, SENIOR)"),
                            fieldWithPath("requiredConsent.privacyPolicyAgreed")
                                .type(BOOLEAN)
                                .description("개인정보 동의"),
                            fieldWithPath("requiredConsent.serviceTermsAgreed")
                                .type(BOOLEAN)
                                .description("서비스 약관 동의"))
                        .build())));
  }

  @Test
  void signInTest() throws Exception {
    Token token = new Token("accessToken", "refreshToken");
    given(responseMapper.toTokenResponse(any()))
        .willReturn(new TokenResponse(token.accessToken(), token.refreshToken()));
    mockMvc
        .perform(
            post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(UserFixture.defaultSignInRequest())))
        .andExpect(status().isOk())
        .andDo(
            MockMvcRestDocumentationWrapper.document(
                "auth-signin",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Auth")
                        .description("사용자 로그인")
                        .requestFields(
                            fieldWithPath("email").type(STRING).description("사용자 이메일"),
                            fieldWithPath("password").type(STRING).description("사용자 비밀번호"))
                        .responseFields(
                            fieldWithPath("data.accessToken").type(STRING).description("엑세스 토큰"),
                            fieldWithPath("data.refreshToken").type(STRING).description("리프레시 토큰"))
                        .build())));
  }

  @Test
  void reissueTest() throws Exception {
    Token token = new Token("accessToken", "refreshToken");
    given(responseMapper.toTokenResponse(any()))
        .willReturn(new TokenResponse(token.accessToken(), token.refreshToken()));

    mockMvc
        .perform(
            post("/auth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(UserFixture.defaultReissueRequest())))
        .andExpect(status().isOk())
        .andDo(
            MockMvcRestDocumentationWrapper.document(
                "auth-reissue",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Auth")
                        .summary("토큰 재발급")
                        .requestFields(
                            fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰"))
                        .responseFields(
                            fieldWithPath("data.accessToken").type(STRING).description("엑세스 토큰"),
                            fieldWithPath("data.refreshToken").type(STRING).description("리프레시 토큰"))
                        .build())));
  }
}
