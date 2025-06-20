package com.growit.app.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.fake.UserFixture;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.controller.mapper.UserRequestMapper;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.usecase.GetUserUseCase;
import com.growit.app.user.usecase.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class UserControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private GetUserUseCase getUserUseCase;

  @MockBean
  private UserRequestMapper mapper;

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
    UserDto userDto = new UserDto(user, jobRole);

    given(getUserUseCase.execute(user)).willReturn(userDto);
    given(mapper.toResponse(userDto)).willReturn(new UserResponse(
        user.getId(),
        user.getEmail().value(),
        user.getName(),
        jobRole,
        user.getCareerYear().name()
    ));

    mockMvc
        .perform(get("/users/myprofile")
            .header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-user",
                new ResourceSnippetParametersBuilder()
                    .tag("User")
                    .description("사용자 조회")
                    .responseFields(
                        fieldWithPath("data.id").type(STRING).description("사용자 ID"),
                        fieldWithPath("data.email").type(STRING).description("이메일"),
                        fieldWithPath("data.name").type(STRING).description("이름"),
                        fieldWithPath("data.jobRole.id").type(STRING).description("직무 ID"),
                        fieldWithPath("data.jobRole.name").type(STRING).description("직무 이름"),
                        fieldWithPath("data.careerYear").type(STRING).description("경력 연차")
                    )));
  }
}
