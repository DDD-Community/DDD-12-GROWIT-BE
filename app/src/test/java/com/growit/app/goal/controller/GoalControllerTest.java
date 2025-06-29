package com.growit.app.goal.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.FakeGoalRepositoryConfig;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@Import(FakeGoalRepositoryConfig.class)
class GoalControllerTest {
  private MockMvc mockMvc;

  @MockitoBean private GetUserGoalsUseCase getUserGoalsUseCase;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private GoalRepository goalRepository;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    TestSecurityUtil.setMockUser();
    if (goalRepository instanceof FakeGoalRepository fake) {
      fake.clear();
    }
  }

  @Test
  void getMyGoal() throws Exception {
    Goal goal = GoalFixture.defaultGoal();
    goalRepository.saveGoal(goal);

    // when & then
    mockMvc
        .perform(get("/goals").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-my-goal",
                new ResourceSnippetParametersBuilder()
                    .tag("Goals")
                    .description("내 목표 조회")
                    .responseFields(
                        fieldWithPath("data[].id").type(STRING).description("목표 ID"),
                        fieldWithPath("data[].name").type(STRING).description("목표 이름"),
                        fieldWithPath("data[].duration").description("기간 정보 객체"),
                        fieldWithPath("data[].duration.startDate")
                            .type(STRING)
                            .description("시작일 (yyyy-MM-dd)"),
                        fieldWithPath("data[].duration.endDate")
                            .type(STRING)
                            .description("종료일 (yyyy-MM-dd)"),
                        fieldWithPath("data[].beforeAfter").description("전/후 상태 정보 객체"),
                        fieldWithPath("data[].beforeAfter.asIs")
                            .type(STRING)
                            .description("현재 상태(As-Is)"),
                        fieldWithPath("data[].beforeAfter.toBe")
                            .type(STRING)
                            .description("목표 달성 후 상태(To-Be)"),
                        fieldWithPath("data[].plans").description("계획 리스트"),
                        fieldWithPath("data[].plans[].id").type(STRING).description("계획 ID"),
                        fieldWithPath("data[].plans[].content")
                            .type(STRING)
                            .description("계획 내용"))));
  }

  @Test
  void createGoal() throws Exception {
    CreateGoalRequest body = GoalFixture.defaultCreateGoalRequest();
    mockMvc
        .perform(
            post("/goals")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andDo(
            document(
                "create-goal",
                new ResourceSnippetParametersBuilder()
                    .tag("Goals")
                    .description("목표 생성")
                    .requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("목표 이름"),
                        fieldWithPath("duration.startDate")
                            .type(JsonFieldType.STRING)
                            .description("시작일 (yyyy-MM-dd)"),
                        fieldWithPath("duration.endDate")
                            .type(JsonFieldType.STRING)
                            .description("종료일 (yyyy-MM-dd)"),
                        fieldWithPath("beforeAfter.asIs")
                            .type(JsonFieldType.STRING)
                            .description("현재 상태(As-Is)"),
                        fieldWithPath("beforeAfter.toBe")
                            .type(JsonFieldType.STRING)
                            .description("목표 달성 후 상태(To-Be)"),
                        fieldWithPath("plans[].content")
                            .type(JsonFieldType.STRING)
                            .description("계획 내용"))
                    .responseFields(fieldWithPath("data.id").type(STRING).description("목표 ID"))));
  }

  @Test
  void deleteGoal() throws Exception {
    Goal goal = GoalFixture.defaultGoal();
    goalRepository.saveGoal(goal);

    mockMvc
        .perform(
            delete("/goals/{id}", goal.getId()).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value("삭제가 완료 되었습니다."))
        .andDo(
            document(
                "delete-goal",
                new ResourceSnippetParametersBuilder()
                    .tag("Goals")
                    .description("목표 삭제")
                    .pathParameters(parameterWithName("id").description("삭제할 목표 ID"))
                    .responseFields(
                        fieldWithPath("data")
                            .type(JsonFieldType.STRING)
                            .description("삭제가 완료 되었습니다."))));
  }
}
