package com.growit.app.goal.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.common.docs.FieldBuilder;
import com.growit.app.goal.controller.GoalDocumentFields;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.FakeGoalRepositoryConfig;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.UpdatePlanCommand;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import com.growit.app.goal.usecase.*;
import java.util.List;
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
@SpringBootTest
@ActiveProfiles("test")
@Import({FakeGoalRepositoryConfig.class, TestSecurityConfig.class})
class GoalControllerTest {
  private static final String TAG = GoalDocumentFields.TAG;

  private MockMvc mockMvc;

  @MockitoBean private GetUserGoalsUseCase getUserGoalsUseCase;
  @MockitoBean private GetGoalUseCase getGoalUseCase;
  @MockitoBean private DeleteGoalUseCase deleteGoalUseCase;
  @MockitoBean private UpdateGoalUseCase updateGoalUseCase;
  @MockitoBean private UpdatePlanUseCase updatePlanUseCase;
  @MockitoBean private RecommendPlanUseCase recommendPlanUseCase;

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
  void getMyGoals() throws Exception {
    given(getUserGoalsUseCase.getMyGoals(any(), eq(GoalStatus.NONE)))
        .willReturn(List.of(GoalFixture.defaultGoal()));

    // when & then
    mockMvc
        .perform(
            get("/goals")
                .param("status", String.valueOf(GoalStatus.NONE))
                .header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-my-goals",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("내 목표 목록 조회")
                        .queryParameters(
                            parameterWithName("status")
                                .description("목표 상태 필터 | Enum: NONE, ENDED, PROGRESS"))
                        .responseFields(GoalDocumentFields.GOALS_LIST_RESPONSE_FIELDS)
                        .build())));
  }

  @Test
  void getMyGoal() throws Exception {
    final Goal goal = GoalFixture.defaultGoal();
    given(getGoalUseCase.getGoal(eq(goal.getId()), any())).willReturn(goal);

    // when & then
    mockMvc
        .perform(get("/goals/{id}", goal.getId()).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-my-goal",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("내 목표 조회")
                        .pathParameters(parameterWithName("id").description("목표 ID"))
                        .responseFields(GoalDocumentFields.GOAL_RESPONSE_FIELDS)
                        .build())));
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
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("목표 생성")
                        .requestFields(GoalDocumentFields.CREATE_GOAL_REQUEST_FIELDS)
                        .responseFields(
                            FieldBuilder.create()
                                .addField("data.id", STRING, "목표 ID")
                                .addField("data.mentor", STRING, "멘토")
                                .build())
                        .build())));
  }

  @Test
  void deleteGoal() throws Exception {
    mockMvc
        .perform(delete("/goals/{id}", "goalId").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "delete-goal",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("목표 삭제")
                        .pathParameters(parameterWithName("id").description("삭제할 목표 ID"))
                        .responseFields(
                            FieldBuilder.create()
                                .addSuccessMessageResponse()
                                .build())
                        .build())));
  }

  @Test
  void updateGoal() throws Exception {
    CreateGoalRequest body = GoalFixture.defaultCreateGoalRequest();
    mockMvc
        .perform(
            put("/goals/{id}", "goalId")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "update-goal",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("목표 수정")
                        .requestFields(GoalDocumentFields.CREATE_GOAL_REQUEST_FIELDS)
                        .responseFields(
                            FieldBuilder.create()
                                .addSuccessMessageResponse()
                                .build())
                        .build())));
  }

  @Test
  void updatePlanContent() throws Exception {
    String goalId = "goal-123";
    String planId = "plan-456";

    String bodyJson =
        """
      {
        "content": "주 3회 운동으로 계획 수정"
      }
      """;

    mockMvc
        .perform(
            put("/goals/me/updatePlan")
                .param("goalId", goalId)
                .param("planId", planId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "update-plan-content",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("계획 내용 수정")
                        .queryParameters(
                            parameterWithName("goalId").description("목표 ID"),
                            parameterWithName("planId").description("계획 ID"))
                        .requestFields(
                            FieldBuilder.create()
                                .addField("content", STRING, "수정할 계획 내용")
                                .build())
                        .responseFields(
                            FieldBuilder.create()
                                .addSuccessMessageResponse()
                                .build())
                        .build())));

    verify(updatePlanUseCase).execute(any(UpdatePlanCommand.class));
  }

  @Test
  void recommendPlan() throws Exception {
    String goalId = "goal-123";
    String planId = "plan-456";
    String expectedRecommendation =
        "계획 " + planId + "에 대한 AI 추천: 단계별로 세분화하여 실행하면 성공 확률이 높아집니다. 매일 30분씩 투자하여 꾸준히 진행하세요.";

    PlanRecommendation mockRecommendation =
        new PlanRecommendation("rec-123", "user-123", goalId, planId, expectedRecommendation);

    given(recommendPlanUseCase.execute(any(), eq(goalId), eq(planId)))
        .willReturn(mockRecommendation);

    mockMvc
        .perform(
            get("/goals/{id}/plans/{planId}/recommendation", goalId, planId)
                .header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(expectedRecommendation))
        .andDo(
            document(
                "recommend-plan",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("계획 추천")
                        .pathParameters(
                            parameterWithName("id").description("목표 ID"),
                            parameterWithName("planId").description("계획 ID"))
                        .responseFields(
                            FieldBuilder.create()
                                .addField("data", STRING, "AI 추천 내용")
                                .build())
                        .build())));

    verify(recommendPlanUseCase).execute(any(), eq(goalId), eq(planId));
  }
}
