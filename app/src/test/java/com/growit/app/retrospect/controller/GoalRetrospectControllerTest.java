package com.growit.app.retrospect.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.retrospect.controller.RetrospectDocumentFields;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.goalretrospect.GoalRetrospectFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.retrospect.controller.goalretrospect.dto.request.CreateGoalRetrospectRequest;
import com.growit.app.retrospect.controller.goalretrospect.dto.request.UpdateGoalRetrospectRequest;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.usecase.goalretrospect.CreateGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.GetFinishedGoalsWithGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.GetGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.UpdateGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.dto.GoalWithGoalRetrospectDto;
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
@Import({TestSecurityConfig.class})
class GoalRetrospectControllerTest {
  private static final String TAG = RetrospectDocumentFields.GOAL_RETROSPECT_TAG;

  private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private CreateGoalRetrospectUseCase createGoalRetrospectUseCase;
  @MockitoBean private UpdateGoalRetrospectUseCase updateGoalRetrospectUseCase;
  @MockitoBean private GetGoalRetrospectUseCase getGoalRetrospectUseCase;

  @MockitoBean
  private GetFinishedGoalsWithGoalRetrospectUseCase getFinishedGoalsWithGoalRetrospectUseCase;

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
  void getGoalRetrospect() throws Exception {
    // given
    String goalRetrospectId = "review-20250808";
    GoalRetrospect goalRetrospect =
        new GoalRetrospectFixture.GoalRetrospectBuilder()
            .goalId("goalId")
            .todoCompletedRate(25)
            .analysis(
                new Analysis(
                    "GROWIT MVP 개발과 서비스 기획을 병행하며 4주 목표를 달성",
                    "모든 활동이 한 가지 핵심 가치에 연결되도록 중심축을 명확히 해보라냥!"))
            .content("이번 달 나는 '나만의 의미 있는 일'을 찾기 위해 다양한 프로젝트와 리서치에 몰입했다...")
            .build();

    given(getGoalRetrospectUseCase.execute(eq(goalRetrospectId), any(String.class)))
        .willReturn(goalRetrospect);

    // when & then
    mockMvc
        .perform(
            get("/goal-retrospects/{id}", goalRetrospectId)
                .header("Authorization", "Bearer mock-jwt-token")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-goal-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("목표회고 단건 조회")
                        .pathParameters(parameterWithName("id").description("목표회고 ID"))
                        .responseFields(RetrospectDocumentFields.GOAL_RETROSPECT_GET_RESPONSE_FIELDS)
                        .build())));
  }

  @Test
  void createGoalRetrospect() throws Exception {
    CreateGoalRetrospectRequest body = new CreateGoalRetrospectRequest("goalId");
    given(createGoalRetrospectUseCase.execute(any())).willReturn("id");

    mockMvc
        .perform(
            post("/goal-retrospects")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andDo(
            document(
                "create-goal-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("목표 회고 생성")
                        .requestFields(RetrospectDocumentFields.GOAL_RETROSPECT_CREATE_REQUEST_FIELDS)
                        .responseFields(RetrospectDocumentFields.GOAL_RETROSPECT_CREATE_RESPONSE_FIELDS)
                        .build())));
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
                        .tag(TAG)
                        .summary("목표 회고 수정")
                        .pathParameters(parameterWithName("id").description("목표 회고 ID"))
                        .requestFields(RetrospectDocumentFields.GOAL_RETROSPECT_UPDATE_REQUEST_FIELDS)
                        .build())));
  }

  @Test
  void getGoalRetrospectsByYear() throws Exception {
    // given
    int year = 2025;
    Goal goal = GoalFixture.defaultGoal();
    GoalRetrospect goalRetrospect = GoalRetrospectFixture.defaultGoalRetrospect();
    given(getFinishedGoalsWithGoalRetrospectUseCase.execute("user-1", year))
        .willReturn(List.of(new GoalWithGoalRetrospectDto(goal, goalRetrospect)));

    // when & then
    mockMvc
        .perform(
            get("/goal-retrospects")
                .header("Authorization", "Bearer mock-jwt-token")
                .param("year", String.valueOf(year))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-goal-retrospects-by-year",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("연도별 목표+회고 목록 조회")
                        .queryParameters(parameterWithName("year").description("조회 연도 (예: 2025)"))
                        .responseFields(RetrospectDocumentFields.GOAL_RETROSPECTS_BY_YEAR_RESPONSE_FIELDS)
                        .build())));
  }
}
