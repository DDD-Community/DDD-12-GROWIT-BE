package com.growit.app.user.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.user.controller.AuthDocumentFields;
import com.growit.app.common.docs.FieldBuilder;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.controller.dto.request.SignUpKaKaoRequest;
import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.controller.mapper.RequestMapper;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.usecase.ReissueUseCase;
import com.growit.app.user.usecase.SignInUseCase;
import com.growit.app.user.usecase.SignUpKaKaoUseCase;
import com.growit.app.user.usecase.SignUpUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class AuthControllerTest {
  private static final String TAG = AuthDocumentFields.TAG;

  private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private SignUpUseCase signUpUseCase;
  @MockitoBean private SignUpKaKaoUseCase signUpKaKaoUseCase;
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
                        .tag(TAG)
                        .summary("사용자 회원가입")
                        .requestFields(
                            FieldBuilder.create()
                                .addFields(AuthDocumentFields.SIGNUP_REQUEST_FIELDS)
                                .addConsentFields()
                                .build())
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
                        .tag(TAG)
                        .description("사용자 로그인")
                        .requestFields(AuthDocumentFields.SIGNIN_REQUEST_FIELDS)
                        .responseFields(
                            FieldBuilder.create().addTokenResponse().build())
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
                        .tag(TAG)
                        .summary("토큰 재발급")
                        .requestFields(AuthDocumentFields.REISSUE_REQUEST_FIELDS)
                        .responseFields(
                            FieldBuilder.create().addTokenResponse().build())
                        .build())));
  }

  @Test
  void signupKakaoTest() throws Exception {
    SignUpKaKaoRequest signUpKaKaoRequest = UserFixture.defaultSignUpKaKaoRequest();
    mockMvc
        .perform(
            post("/auth/signup/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpKaKaoRequest)))
        .andExpect(status().isCreated())
        .andDo(
            MockMvcRestDocumentationWrapper.document(
                "auth-signup-kakao",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("카카오 회원가입")
                        .requestFields(
                            FieldBuilder.create()
                                .addFields(AuthDocumentFields.SIGNUP_KAKAO_REQUEST_FIELDS)
                                .addConsentFields()
                                .build())
                        .build())));
  }
}
