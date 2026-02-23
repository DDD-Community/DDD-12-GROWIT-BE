package com.growit.app.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.BOOLEAN;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.controller.dto.request.RegisterPromotionRequest;
import com.growit.app.user.controller.dto.request.UpdateUserRequest;
import com.growit.app.user.controller.dto.response.SajuInfoResponse;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.EarthlyBranchHour;
import com.growit.app.user.domain.user.vo.SajuInfo;
import com.growit.app.user.usecase.DeleteUserUseCase;
import com.growit.app.user.usecase.GetUserUseCase;
import com.growit.app.user.usecase.LogoutUseCase;
import com.growit.app.user.usecase.RegisterPromotionUseCase;
import com.growit.app.user.usecase.UpdateUserUseCase;
import java.time.LocalDate;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class UserControllerTest {

  private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;
  @MockitoBean private ResponseMapper responseMapper;

  @MockitoBean private GetUserUseCase getUserUseCase;
  @MockitoBean private UpdateUserUseCase updateUserUseCase;
  @MockitoBean private LogoutUseCase logoutUseCase;
  @MockitoBean private DeleteUserUseCase deleteUserUseCase;
  @MockitoBean private RegisterPromotionUseCase registerPromotionUseCase;

  @BeforeEach
  void setUp(
      WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentationContextProvider) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider))
            .build();
    TestSecurityUtil.setMockUser();
  }

  @Test
  void getUser() throws Exception {
    User user = UserFixture.defaultUser();
    given(responseMapper.toUserResponse(any()))
        .willReturn(
            new UserResponse(
                user.getId(),
                user.getEmail().value(),
                user.getName(),
                user.getLastName(),
                null,
                null,
                null));

    mockMvc
        .perform(get("/users/myprofile").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("사용자 조회 (사주 정보 없음)")
                        .description("사주 정보가 없는 사용자의 정보를 조회합니다.")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .responseFields(
                            fieldWithPath("data.id").type(STRING).description("사용자 ID"),
                            fieldWithPath("data.email").type(STRING).description("이메일"),
                            fieldWithPath("data.name").type(STRING).description("이름"),
                            fieldWithPath("data.lastName").type(STRING).description("성").optional(),
                            fieldWithPath("data.saju").description("사주정보").optional(),
                            fieldWithPath("data.saju.gender")
                                .type(STRING)
                                .description("성별")
                                .optional(),
                            fieldWithPath("data.saju.birth")
                                .type(STRING)
                                .description("생년월일")
                                .optional(),
                            fieldWithPath("data.saju.birthHour")
                                .type(STRING)
                                .description("태어난 시간")
                                .optional(),
                            fieldWithPath("data.careerYear")
                                .type(STRING)
                                .description("연차 (Deprecated)")
                                .optional(),
                            fieldWithPath("data.jobRoleId")
                                .type(STRING)
                                .description("직무 ID (Deprecated)")
                                .optional())
                        .build())));
  }

  @Test
  void getUserWithSaju() throws Exception {
    User user = UserFixture.defaultUser();
    given(responseMapper.toUserResponse(any()))
        .willReturn(
            new UserResponse(
                user.getId(),
                user.getEmail().value(),
                user.getName(),
                user.getLastName(),
                SajuInfoResponse.builder()
                    .gender(SajuInfo.Gender.MALE)
                    .birth(LocalDate.of(1990, 5, 15))
                    .birthHour(EarthlyBranchHour.JIN)
                    .build(),
                null,
                null));

    mockMvc
        .perform(get("/users/myprofile").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-user-with-saju",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("사용자 조회 (사주 정보 포함)")
                        .description("사주 정보가 있는 사용자의 정보를 조회합니다.")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .responseFields(
                            fieldWithPath("data.id").type(STRING).description("사용자 ID"),
                            fieldWithPath("data.email").type(STRING).description("이메일"),
                            fieldWithPath("data.name").type(STRING).description("이름"),
                            fieldWithPath("data.lastName").type(STRING).description("성").optional(),
                            fieldWithPath("data.saju").description("사주정보"),
                            fieldWithPath("data.saju.gender")
                                .type(STRING)
                                .description("성별 (MALE, FEMALE)"),
                            fieldWithPath("data.saju.birth")
                                .type(STRING)
                                .description("생년월일 (YYYY-MM-DD)"),
                            fieldWithPath("data.saju.birthHour")
                                .type(STRING)
                                .description(
                                    "태어난 시간 (JA, CHUK, IN, MYO, JIN, SA, O, MI, SIN, YU, SUL, HAE)"),
                            fieldWithPath("data.careerYear")
                                .type(STRING)
                                .description("연차 (Deprecated)")
                                .optional(),
                            fieldWithPath("data.jobRoleId")
                                .type(STRING)
                                .description("직무 ID (Deprecated)")
                                .optional())
                        .build())));
  }

  @Test
  void updateUser() throws Exception {
    UpdateUserRequest request = UserFixture.defaultUpdateUserRequest();

    mockMvc
        .perform(
            put("/users/myprofile")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "update-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("사용자 업데이트")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .requestFields(
                            fieldWithPath("name").type(STRING).description("이름"),
                            fieldWithPath("lastName").type(STRING).description("성").optional(),
                            fieldWithPath("saju").description("사주정보").optional(),
                            fieldWithPath("saju.gender").type(STRING).description("성별").optional(),
                            fieldWithPath("saju.birth").type(STRING).description("생년월일").optional(),
                            fieldWithPath("saju.birthHour")
                                .type(STRING)
                                .description("태어난 시간")
                                .optional(),
                            fieldWithPath("careerYear")
                                .type(STRING)
                                .description("연차 (Deprecated)")
                                .optional(),
                            fieldWithPath("jobRoleId")
                                .type(STRING)
                                .description("직무 ID (Deprecated)")
                                .optional())
                        .responseFields(
                            fieldWithPath("data").type(STRING).description("업데이트 성공 메세지"))
                        .build())));
  }

  @Test
  void logout() throws Exception {
    mockMvc
        .perform(
            post("/users/myprofile/logout")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "logout-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("사용자 로그아웃")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .responseFields(
                            fieldWithPath("data").type(STRING).description("로그아웃 성공 메세지"))
                        .build())));
  }

  @Test
  void deleteUser() throws Exception {
    mockMvc
        .perform(
            delete("/users/myprofile")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType("application/json"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "delete-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("사용자 탈퇴")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .responseFields(fieldWithPath("data").type(STRING).description("탈퇴 성공 메세지"))
                        .build())));
  }

  @Test
  void getOnboarding() throws Exception {
    mockMvc
        .perform(
            get("/users/myprofile/onboarding").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-user-onboarding",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("온보딩 여부 조회")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .responseFields(
                            fieldWithPath("data").type(BOOLEAN).description("온보딩 진행 여부"))
                        .build())));
  }

  @Test
  void completeOnboarding() throws Exception {
    mockMvc
        .perform(
            put("/users/myprofile/onboarding").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "complete-user-onboarding",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("온보딩 완료 처리")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .responseFields(
                            fieldWithPath("data").type(STRING).description("온보딩 완료 성공 메세지"))
                        .build())));
    verify(updateUserUseCase).isOnboarding(any(User.class));
  }

  @Test
  void registerPromotion() throws Exception {
    RegisterPromotionRequest request = new RegisterPromotionRequest("PROMO2024");

    mockMvc
        .perform(
            post("/users/myprofile/promotion")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "register-promotion",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("프로모션 등록")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .requestFields(fieldWithPath("code").type(STRING).description("프로모션 코드"))
                        .responseFields(
                            fieldWithPath("data").type(STRING).description("프로모션 등록 성공 메세지"))
                        .build())));

    verify(registerPromotionUseCase).execute(any(User.class), any(String.class));
  }

  @Test
  void updateUserWithSaju() throws Exception {
    UpdateUserRequest request = UserFixture.defaultUpdateUserSajuRequest();

    mockMvc
        .perform(
            put("/users/myprofile")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "update-user-with-saju",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("User")
                        .summary("사용자 정보 업데이트 (사주 정보 포함)")
                        .description(
                            "사용자의 기본 정보와 사주 정보를 함께 업데이트합니다. 사주 정보에는 성별, 생년월일, 태어난 시간이 포함됩니다.")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .requestFields(
                            fieldWithPath("name").type(STRING).description("이름"),
                            fieldWithPath("lastName").type(STRING).description("성").optional(),
                            fieldWithPath("saju").description("사주정보"),
                            fieldWithPath("saju.gender")
                                .type(STRING)
                                .description("성별 (MALE, FEMALE)"),
                            fieldWithPath("saju.birth")
                                .type(STRING)
                                .description("생년월일 (YYYY-MM-DD 형식)"),
                            fieldWithPath("saju.birthHour")
                                .type(STRING)
                                .description(
                                    "태어난 시간 (JA: 자시, CHUK: 축시, IN: 인시, MYO: 묘시, JIN: 진시, SA: 사시, O: 오시, MI: 미시, SIN: 신시, YU: 유시, SUL: 술시, HAE: 해시)"),
                            fieldWithPath("careerYear")
                                .type(STRING)
                                .description("연차 (Deprecated)")
                                .optional(),
                            fieldWithPath("jobRoleId")
                                .type(STRING)
                                .description("직무 ID (Deprecated)")
                                .optional())
                        .responseFields(
                            fieldWithPath("data").type(STRING).description("업데이트 성공 메세지"))
                        .build())));
  }
}
