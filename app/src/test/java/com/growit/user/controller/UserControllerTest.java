package com.growit.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.fake.user.UserFixture;
import com.growit.user.controller.dto.response.UserResponse;
import com.growit.user.controller.mapper.ResponseMapper;
import com.growit.user.domain.jobrole.JobRole;
import com.growit.user.domain.user.User;
import com.growit.user.usecase.GetUserUseCase;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
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
class UserControllerTest {

  private MockMvc mockMvc;

  @MockitoBean private GetUserUseCase getUserUseCase;

  @MockitoBean private ResponseMapper mapper;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
  }

  @Test
  void getUserTest() throws Exception {
    User user = UserFixture.defaultUser();
    JobRole jobRole = new JobRole("dev", "개발자");
    given(mapper.toUserResponse(any()))
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
}
