package com.growit.app.goal.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.NUMBER;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.config.TestOAuthConfig;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.FakeGoalRepositoryConfig;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.UpdatePlanCommand;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@Import({FakeGoalRepositoryConfig.class, TestOAuthConfig.class})
class GoalControllerTest {
  private MockMvc mockMvc;

  @MockitoBean private GetUserGoalsUseCase getUserGoalsUseCase;
  @MockitoBean private GetGoalUseCase getGoalUseCase;
  @MockitoBean private DeleteGoalUseCase deleteGoalUseCase;
  @MockitoBean private UpdateGoalUseCase updateGoalUseCase;
  @MockitoBean private UpdatePlanUseCase updatePlanUseCase;

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
                        .tag("Goals")
                        .summary("내 목표 목록 조회")
                        .queryParameters(
                            parameterWithName("status")
                                .description("목표 상태 필터 | Enum: NONE, ENDED, PROGRESS"))
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
                            fieldWithPath("data[].toBe").type(STRING).description("목표 달성 후 상태"),
                            fieldWithPath("data[].category")
                                .type(STRING)
                                .description(
                                    "목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"),
                            fieldWithPath("data[].plans").description("계획 리스트"),
                            fieldWithPath("data[].plans[].id").type(STRING).description("계획 ID"),
                            fieldWithPath("data[].plans[].weekOfMonth")
                                .type(NUMBER)
                                .description("주차"),
                            fieldWithPath("data[].plans[].duration").description("기간 정보 객체"),
                            fieldWithPath("data[].plans[].duration.startDate")
                                .type(STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("data[].plans[].duration.endDate")
                                .type(STRING)
                                .description("종료일 (yyyy-MM-dd)"),
                            fieldWithPath("data[].plans[].content")
                                .type(STRING)
                                .description("계획 내용"))
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
                        .tag("Goals")
                        .summary("내 목표 조회")
                        .pathParameters(parameterWithName("id").description("목표 ID"))
                        .responseFields(
                            fieldWithPath("data.id").type(STRING).description("목표 ID"),
                            fieldWithPath("data.name").type(STRING).description("목표 이름"),
                            fieldWithPath("data.duration").description("기간 정보 객체"),
                            fieldWithPath("data.duration.startDate")
                                .type(STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("data.duration.endDate")
                                .type(STRING)
                                .description("종료일 (yyyy-MM-dd)"),
                            fieldWithPath("data.toBe").type(STRING).description("목표 달성 후 상태"),
                            fieldWithPath("data.category")
                                .type(STRING)
                                .description(
                                    "목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"),
                            fieldWithPath("data.plans").description("계획 리스트"),
                            fieldWithPath("data.plans[].id").type(STRING).description("계획 ID"),
                            fieldWithPath("data.plans[].weekOfMonth")
                                .type(NUMBER)
                                .description("주차"),
                            fieldWithPath("data.plans[].duration").description("기간 정보 객체"),
                            fieldWithPath("data.plans[].duration.startDate")
                                .type(STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("data.plans[].duration.endDate")
                                .type(STRING)
                                .description("종료일 (yyyy-MM-dd)"),
                            fieldWithPath("data.plans[].content").type(STRING).description("계획 내용"))
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
                        .tag("Goals")
                        .summary("목표 생성")
                        .requestFields(
                            fieldWithPath("name").type(JsonFieldType.STRING).description("목표 이름"),
                            fieldWithPath("duration.startDate")
                                .type(JsonFieldType.STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("duration.endDate")
                                .type(JsonFieldType.STRING)
                                .description("종료일 (yyyy-MM-dd)"),
                            fieldWithPath("toBe")
                                .type(JsonFieldType.STRING)
                                .description("목표 달성 후 상태"),
                            fieldWithPath("category")
                                .type(JsonFieldType.STRING)
                                .description(
                                    "목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"),
                            fieldWithPath("plans[].weekOfMonth")
                                .type(JsonFieldType.NUMBER)
                                .description("계획 주차"),
                            fieldWithPath("plans[].content")
                                .type(JsonFieldType.STRING)
                                .description("계획 내용"))
                        .responseFields(fieldWithPath("data.id").type(STRING).description("목표 ID"))
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
                        .tag("Goals")
                        .summary("목표 삭제")
                        .pathParameters(parameterWithName("id").description("삭제할 목표 ID"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.STRING)
                                .description("삭제가 완료 되었습니다."))
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
                        .tag("Goals")
                        .summary("목표 수정")
                        .requestFields(
                            fieldWithPath("name").type(JsonFieldType.STRING).description("목표 이름"),
                            fieldWithPath("duration.startDate")
                                .type(JsonFieldType.STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("duration.endDate")
                                .type(JsonFieldType.STRING)
                                .description("종료일 (yyyy-MM-dd)"),
                            fieldWithPath("toBe")
                                .type(JsonFieldType.STRING)
                                .description("목표 달성 후 상태"),
                            fieldWithPath("category")
                                .type(JsonFieldType.STRING)
                                .description(
                                    "목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"),
                            fieldWithPath("plans[].weekOfMonth")
                                .type(JsonFieldType.NUMBER)
                                .description("계획 주차"),
                            fieldWithPath("plans[].content")
                                .type(JsonFieldType.STRING)
                                .description("계획 내용"))
                        .responseFields(fieldWithPath("data").type(STRING).description("성공 메세지"))
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
                        .tag("Goals")
                        .summary("계획 내용 수정")
                        .queryParameters(
                            parameterWithName("goalId").description("목표 ID"),
                            parameterWithName("planId").description("계획 ID"))
                        .requestFields(
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("수정할 계획 내용"))
                        .responseFields(fieldWithPath("data").type(STRING).description("성공 메세지"))
                        .build())));

    verify(updatePlanUseCase).execute(any(UpdatePlanCommand.class));
  }
}
