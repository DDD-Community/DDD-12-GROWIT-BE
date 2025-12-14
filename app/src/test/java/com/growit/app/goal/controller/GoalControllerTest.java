package com.growit.app.goal.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.FakeGoalRepositoryConfig;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.goal.dto.CreateGoalResult;
import com.growit.app.goal.usecase.*;
import com.growit.app.goal.usecase.dto.GoalWithAnalysisDto;
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
  private MockMvc mockMvc;

  @MockitoBean private GetUserGoalsUseCase getUserGoalsUseCase;
  @MockitoBean private GetGoalUseCase getGoalUseCase;
  @MockitoBean private DeleteGoalUseCase deleteGoalUseCase;
  @MockitoBean private UpdateGoalUseCase updateGoalUseCase;
  @MockitoBean private CreateGoalUseCase createGoalUseCase;

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
        .willReturn(List.of(GoalFixture.defaultGoalWithAnalysis()));

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
                        .tag("Goal")
                        .summary("내 목표 목록 조회")
                        .queryParameters(
                            parameterWithName("status")
                                .description("목표 상태 필터 | Enum: NONE, ENDED, PROGRESS")
                                .optional())
                        .responseFields(
                            fieldWithPath("data[].id").type(STRING).description("목표 ID"),
                            fieldWithPath("data[].name").type(STRING).description("목표 이름"),
                            fieldWithPath("data[].planet").description("행성 정보 객체"),
                            fieldWithPath("data[].planet.name").type(STRING).description("행성 이름"),
                            fieldWithPath("data[].planet.image").description("행성 이미지 정보"),
                            fieldWithPath("data[].planet.image.done").type(STRING).description("완료 이미지 URL"),
                            fieldWithPath("data[].planet.image.progress").type(STRING).description("진행중 이미지 URL"),
                            fieldWithPath("data[].duration").description("기간 정보 객체"),
                            fieldWithPath("data[].duration.startDate")
                                .type(STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("data[].duration.endDate")
                                .type(STRING)
                                .description("종료일 (yyyy-MM-dd)"),
                            fieldWithPath("data[].status").type(STRING).description("목표 상태 (PROGRESS/ENDED)"),
                            fieldWithPath("data[].analysis").description("분석 정보 객체"),
                            fieldWithPath("data[].analysis.todoCompletedRate").type(JsonFieldType.NUMBER).description("할일 완료율"),
                            fieldWithPath("data[].analysis.summary").type(STRING).description("분석 요약"),
                            fieldWithPath("data[].isChecked").type(JsonFieldType.BOOLEAN).description("목표 완료 여부"))
                        .build())));
  }

  @Test
  void getMyGoal() throws Exception {
    final GoalWithAnalysisDto goalWithAnalysis = GoalFixture.defaultGoalWithAnalysis();
    given(getGoalUseCase.getGoal(eq(goalWithAnalysis.getGoal().getId()), any())).willReturn(goalWithAnalysis);

    // when & then
    mockMvc
        .perform(get("/goals/{id}", goalWithAnalysis.getGoal().getId()).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-my-goal",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Goal")
                        .summary("내 목표 조회")
                        .pathParameters(parameterWithName("id").description("목표 ID"))
                        .responseFields(
                            fieldWithPath("data.id").type(STRING).description("목표 ID"),
                            fieldWithPath("data.name").type(STRING).description("목표 이름"),
                            fieldWithPath("data.planet").description("행성 정보 객체"),
                            fieldWithPath("data.planet.name").type(STRING).description("행성 이름"),
                            fieldWithPath("data.planet.image").description("행성 이미지 정보"),
                            fieldWithPath("data.planet.image.done").type(STRING).description("완료 이미지 URL"),
                            fieldWithPath("data.planet.image.progress").type(STRING).description("진행중 이미지 URL"),
                            fieldWithPath("data.duration").description("기간 정보 객체"),
                            fieldWithPath("data.duration.startDate")
                                .type(STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("data.duration.endDate")
                                .type(STRING)
                                .description("종료일 (yyyy-MM-dd)"),
                            fieldWithPath("data.status").type(STRING).description("목표 상태 (PROGRESS/ENDED)"),
                            fieldWithPath("data.analysis").description("분석 정보 객체"),
                            fieldWithPath("data.analysis.todoCompletedRate").type(JsonFieldType.NUMBER).description("할일 완료율"),
                            fieldWithPath("data.analysis.summary").type(STRING).description("분석 요약"),
                            fieldWithPath("data.isChecked").type(JsonFieldType.BOOLEAN).description("목표 완료 여부"))
                        .build())));
  }

  @Test
  void createGoal() throws Exception {
    CreateGoalRequest body = GoalFixture.defaultCreateGoalRequest();
    GoalWithAnalysisDto goalWithAnalysis = GoalFixture.defaultGoalWithAnalysis();
    CreateGoalResult result = new CreateGoalResult(goalWithAnalysis.getGoal().getId());
    
    given(createGoalUseCase.execute(any())).willReturn(result);
    given(getGoalUseCase.getGoal(eq(result.id()), any())).willReturn(goalWithAnalysis);
    
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
                        .tag("Goal")
                        .summary("목표 생성")
                        .requestFields(
                            fieldWithPath("name").type(JsonFieldType.STRING).description("목표 이름"),
                            fieldWithPath("duration.startDate")
                                .type(JsonFieldType.STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("duration.endDate")
                                .type(JsonFieldType.STRING)
                                .description("종료일 (yyyy-MM-dd)"))
                        .responseFields(
                            fieldWithPath("data.id").type(STRING).description("목표 ID"),
                            fieldWithPath("data.planet").description("행성 정보 객체"),
                            fieldWithPath("data.planet.name").type(STRING).description("행성 이름"),
                            fieldWithPath("data.planet.image").description("행성 이미지 정보"),
                            fieldWithPath("data.planet.image.done").type(STRING).description("완료 이미지 URL"),
                            fieldWithPath("data.planet.image.progress").type(STRING).description("진행중 이미지 URL"))
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
                        .tag("Goal")
                        .summary("목표 삭제")
                        .pathParameters(parameterWithName("id").description("삭제할 목표 ID"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.STRING)
                                .description("삭제되었습니다."))
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
                        .tag("Goal")
                        .summary("목표 수정")
                        .requestFields(
                            fieldWithPath("name").type(JsonFieldType.STRING).description("목표 이름"),
                            fieldWithPath("duration.startDate")
                                .type(JsonFieldType.STRING)
                                .description("시작일 (yyyy-MM-dd)"),
                            fieldWithPath("duration.endDate")
                                .type(JsonFieldType.STRING)
                                .description("종료일 (yyyy-MM-dd)"))
                        .responseFields(fieldWithPath("data").type(STRING).description("목표가 수정 완료되었습니다."))
                        .build())));
  }
}