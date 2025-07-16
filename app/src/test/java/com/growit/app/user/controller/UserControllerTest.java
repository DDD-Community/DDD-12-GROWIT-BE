package com.growit.app.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.user.controller.dto.request.UpdateUserRequest;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.usecase.DeleteUserUseCase;
import com.growit.app.user.usecase.GetUserUseCase;
import com.growit.app.user.usecase.LogoutUseCase;
import com.growit.app.user.usecase.UpdateUserUseCase;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
class UserControllerTest {

  private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;
  @MockitoBean private ResponseMapper responseMapper;

  @MockitoBean private GetUserUseCase getUserUseCase;
  @MockitoBean private UpdateUserUseCase updateUserUseCase;
  @MockitoBean private LogoutUseCase logoutUseCase;
  @MockitoBean private DeleteUserUseCase deleteUserUseCase;

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
    JobRole jobRole = new JobRole("dev", "개발자");
    given(responseMapper.toUserResponse(any()))
        .willReturn(
            new UserResponse(
                user.getId(),
                user.getEmail().value(),
                user.getName(),
                jobRole,
                user.getCareerYear().name()));

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
                        .summary("사용자 조회")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION)
                                .attributes(key("type").value("String"))
                                .description("JWT (Your Token)"))
                        .responseFields(
                            fieldWithPath("data.id").type(STRING).description("사용자 ID"),
                            fieldWithPath("data.email").type(STRING).description("이메일"),
                            fieldWithPath("data.name").type(STRING).description("이름"),
                            fieldWithPath("data.jobRole.id").type(STRING).description("직무 ID"),
                            fieldWithPath("data.jobRole.name").type(STRING).description("직무 이름"),
                            fieldWithPath("data.careerYear").type(STRING).description("경력 연차"))
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
                            fieldWithPath("jobRoleId").type(STRING).description("직무 ID"),
                            fieldWithPath("careerYear").type(STRING).description("경력 연차"))
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
                "logout-user",
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
}
