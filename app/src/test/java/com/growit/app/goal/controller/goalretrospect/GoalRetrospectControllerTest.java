package com.growit.app.goal.controller.goalretrospect;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.goal.controller.goalretrospect.dto.UpdateGoalRetrospectRequest;
import com.growit.app.retrospect.usecase.UpdateGoalRetrospectUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
class GoalRetrospectControllerTest {
  private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private UpdateGoalRetrospectUseCase updateGoalRetrospectUseCase;

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
  void updateGoalRetrospect() throws Exception {
    UpdateGoalRetrospectRequest body = new UpdateGoalRetrospectRequest("내 목표는 그로잇 완성");
    mockMvc
        .perform(
            patch("/goal-retrospects/{id}", "goal-retrospect-id")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "update-goal-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Goal Retrospects")
                        .summary("목표 회고 수정")
                        .pathParameters(parameterWithName("id").description("목표 회고 ID"))
                        .requestFields(
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("회고 내용"))
                        .build())));
  }
}
