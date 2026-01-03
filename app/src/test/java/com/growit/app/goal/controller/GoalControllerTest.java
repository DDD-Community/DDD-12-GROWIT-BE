package com.growit.app.goal.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.mapper.GoalRequestMapper;
import com.growit.app.goal.controller.mapper.GoalResponseMapper;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.CreateGoalAnalysisUseCase;
import com.growit.app.goal.usecase.CreateGoalUseCase;
import com.growit.app.goal.usecase.DeleteGoalUseCase;
import com.growit.app.goal.usecase.GetGoalUseCase;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.goal.usecase.UpdateGoalUseCase;
import com.growit.app.goal.usecase.dto.GoalWithAnalysisDto;
import com.growit.app.user.domain.user.User;
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
@Import(TestSecurityConfig.class)
class GoalControllerTest {
  private MockMvc mockMvc;

  @MockitoBean private CreateGoalUseCase createGoalUseCase;
  @MockitoBean private GetGoalUseCase getGoalUseCase;
  @MockitoBean private GetUserGoalsUseCase getUserGoalsUseCase;
  @MockitoBean private DeleteGoalUseCase deleteGoalUseCase;
  @MockitoBean private UpdateGoalUseCase updateGoalUseCase;
  @MockitoBean private CreateGoalAnalysisUseCase createGoalAnalysisUseCase;
  @MockitoBean private GoalRequestMapper goalRequestMapper;
  @MockitoBean private GoalResponseMapper goalResponseMapper;

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    TestSecurityUtil.setMockUser();
  }

  @Test
  void createGoal() throws Exception {
    // given
    CreateGoalRequest request = GoalFixture.defaultCreateGoalRequest();

    given(goalRequestMapper.toCommand(any(String.class), any(CreateGoalRequest.class)))
        .willReturn(GoalFixture.defaultCreateGoalCommand());
    given(createGoalUseCase.execute(any())).willReturn(GoalFixture.defaultCreateGoalResult());
    given(getGoalUseCase.getGoal(any(String.class), any()))
        .willReturn(GoalFixture.defaultGoalWithAnalysis());
    given(goalResponseMapper.toCreateResponse(any()))
        .willReturn(GoalFixture.defaultGoalCreateResponse());

    // when & then
    mockMvc
        .perform(
            post("/goals")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "create-goal",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Goals")
                        .summary("목표 생성")
                        .description("새로운 목표를 생성합니다.")
                        .requestFields(
                            fieldWithPath("name").type(JsonFieldType.STRING).description("목표 이름"),
                            fieldWithPath("duration")
                                .type(JsonFieldType.OBJECT)
                                .description("목표 기간 객체"),
                            fieldWithPath("duration.startDate")
                                .type(JsonFieldType.STRING)
                                .description("시작 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("duration.endDate")
                                .type(JsonFieldType.STRING)
                                .description("종료 날짜 (yyyy-MM-dd)"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.OBJECT)
                                .description("생성된 목표 정보"),
                            fieldWithPath("data.id")
                                .type(JsonFieldType.STRING)
                                .description("생성된 목표 ID"),
                            fieldWithPath("data.planet")
                                .type(JsonFieldType.OBJECT)
                                .description("할당된 행성 정보"),
                            fieldWithPath("data.planet.name")
                                .type(JsonFieldType.STRING)
                                .description("행성 이름"),
                            fieldWithPath("data.planet.image")
                                .type(JsonFieldType.OBJECT)
                                .description("행성 이미지 정보"),
                            fieldWithPath("data.planet.image.done")
                                .type(JsonFieldType.STRING)
                                .description("완료 이미지 URL"),
                            fieldWithPath("data.planet.image.progress")
                                .type(JsonFieldType.STRING)
                                .description("진행 이미지 URL"))
                        .build())));
  }

  @Test
  void getMyGoals() throws Exception {
    // given
    List<GoalWithAnalysisDto> goalList = List.of(GoalFixture.defaultGoalWithAnalysis());

    given(getUserGoalsUseCase.getMyGoals(any(User.class), eq(GoalStatus.PROGRESS)))
        .willReturn(goalList);
    given(goalResponseMapper.mapToGoalStatus(any())).willReturn(GoalStatus.PROGRESS);
    given(goalResponseMapper.toDetailResponse(any(), any()))
        .willReturn(GoalFixture.defaultGoalDetailResponse());

    // when & then
    mockMvc
        .perform(
            get("/goals")
                .param("status", "PROGRESS")
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
                        .description("사용자의 목표 목록을 상태별로 조회합니다.")
                        .queryParameters(
                            parameterWithName("status").description("목표 상태 (PROGRESS, ENDED)"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.ARRAY)
                                .description("목록 데이터 배열"),
                            fieldWithPath("data[].id")
                                .type(JsonFieldType.STRING)
                                .description("목표 ID"),
                            fieldWithPath("data[].name")
                                .type(JsonFieldType.STRING)
                                .description("목표 이름"),
                            fieldWithPath("data[].planet")
                                .type(JsonFieldType.OBJECT)
                                .description("행성 정보"),
                            fieldWithPath("data[].planet.name")
                                .type(JsonFieldType.STRING)
                                .description("행성 이름"),
                            fieldWithPath("data[].planet.image")
                                .type(JsonFieldType.OBJECT)
                                .description("행성 이미지 정보"),
                            fieldWithPath("data[].planet.image.done")
                                .type(JsonFieldType.STRING)
                                .description("완료 이미지 URL"),
                            fieldWithPath("data[].planet.image.progress")
                                .type(JsonFieldType.STRING)
                                .description("진행 이미지 URL"),
                            fieldWithPath("data[].duration")
                                .type(JsonFieldType.OBJECT)
                                .description("목표 기간 정보"),
                            fieldWithPath("data[].duration.startDate")
                                .type(JsonFieldType.STRING)
                                .description("시작 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data[].duration.endDate")
                                .type(JsonFieldType.STRING)
                                .description("종료 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data[].status")
                                .type(JsonFieldType.STRING)
                                .description("목표 상태 (PROGRESS, ENDED)"),
                            fieldWithPath("data[].analysis")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("목표 분석 정보 (null 가능)"),
                            fieldWithPath("data[].analysis.todoCompletedRate")
                                .type(JsonFieldType.NUMBER)
                                .optional()
                                .description("ToDo 완료율 (0-100)"),
                            fieldWithPath("data[].analysis.summary")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("분석 요약 메시지"),
                            fieldWithPath("data[].isChecked")
                                .type(JsonFieldType.BOOLEAN)
                                .description("목표 확인 여부"))
                        .build())));
  }

  @Test
  void getMyGoal() throws Exception {
    // given
    String goalId = "goal-123";
    GoalWithAnalysisDto goalWithAnalysis = GoalFixture.defaultGoalWithAnalysis();

    given(getGoalUseCase.getGoal(eq(goalId), any(User.class))).willReturn(goalWithAnalysis);
    given(goalResponseMapper.toDetailResponse(any(), any()))
        .willReturn(GoalFixture.defaultGoalDetailResponse());

    // when & then
    mockMvc
        .perform(get("/goals/{id}", goalId).header("Authorization", "Bearer mock-jwt-token"))
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
                        .summary("특정 목표 조회")
                        .description("특정 목표의 상세 정보를 조회합니다.")
                        .pathParameters(parameterWithName("id").description("목표 ID"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.OBJECT)
                                .description("목표 상세 정보"),
                            fieldWithPath("data.id")
                                .type(JsonFieldType.STRING)
                                .description("목표 ID"),
                            fieldWithPath("data.name")
                                .type(JsonFieldType.STRING)
                                .description("목표 이름"),
                            fieldWithPath("data.planet")
                                .type(JsonFieldType.OBJECT)
                                .description("행성 정보"),
                            fieldWithPath("data.planet.name")
                                .type(JsonFieldType.STRING)
                                .description("행성 이름"),
                            fieldWithPath("data.planet.image")
                                .type(JsonFieldType.OBJECT)
                                .description("행성 이미지 정보"),
                            fieldWithPath("data.planet.image.done")
                                .type(JsonFieldType.STRING)
                                .description("완료 이미지 URL"),
                            fieldWithPath("data.planet.image.progress")
                                .type(JsonFieldType.STRING)
                                .description("진행 이미지 URL"),
                            fieldWithPath("data.duration")
                                .type(JsonFieldType.OBJECT)
                                .description("목표 기간 정보"),
                            fieldWithPath("data.duration.startDate")
                                .type(JsonFieldType.STRING)
                                .description("시작 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data.duration.endDate")
                                .type(JsonFieldType.STRING)
                                .description("종료 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data.status")
                                .type(JsonFieldType.STRING)
                                .description("목표 상태 (PROGRESS, ENDED)"),
                            fieldWithPath("data.analysis")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("목표 분석 정보 (null 가능)"),
                            fieldWithPath("data.analysis.todoCompletedRate")
                                .type(JsonFieldType.NUMBER)
                                .optional()
                                .description("ToDo 완료율 (0-100)"),
                            fieldWithPath("data.analysis.summary")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("분석 요약 메시지"),
                            fieldWithPath("data.isChecked")
                                .type(JsonFieldType.BOOLEAN)
                                .description("목표 확인 여부"))
                        .build())));
  }

  @Test
  void updateGoal() throws Exception {
    // given
    String goalId = "goal-123";
    CreateGoalRequest request = GoalFixture.defaultCreateGoalRequest();
    willDoNothing().given(updateGoalUseCase).execute(any(UpdateGoalCommand.class));

    // when & then
    mockMvc
        .perform(
            put("/goals/{id}", goalId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "update-goal",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Goals")
                        .summary("목표 수정")
                        .description("기존 목표 정보를 수정합니다.")
                        .pathParameters(parameterWithName("id").description("목표 ID"))
                        .requestFields(
                            fieldWithPath("name")
                                .type(JsonFieldType.STRING)
                                .description("수정할 목표 이름"),
                            fieldWithPath("duration")
                                .type(JsonFieldType.OBJECT)
                                .description("수정할 목표 기간 객체"),
                            fieldWithPath("duration.startDate")
                                .type(JsonFieldType.STRING)
                                .description("수정할 시작 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("duration.endDate")
                                .type(JsonFieldType.STRING)
                                .description("수정할 종료 날짜 (yyyy-MM-dd)"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.STRING)
                                .description("수정 완료 메시지"))
                        .build())));
  }

  @Test
  void deleteGoal() throws Exception {
    // given
    String goalId = "goal-123";

    willDoNothing().given(deleteGoalUseCase).execute(any());

    // when & then
    mockMvc
        .perform(delete("/goals/{id}", goalId).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "delete-goal",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Goals")
                        .summary("목표 삭제")
                        .description("특정 목표를 삭제합니다.")
                        .pathParameters(parameterWithName("id").description("삭제할 목표 ID"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.STRING)
                                .description("삭제 완료 메시지"))
                        .build())));
  }

  @Test
  void createGoalAnalysis() throws Exception {
    // given
    String goalId = "goal-123";
    willDoNothing().given(createGoalAnalysisUseCase).execute(eq(goalId), any(String.class));

    // when & then
    mockMvc
        .perform(
            post("/goals/{id}/analysis", goalId).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "create-goal-analysis",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Goals")
                        .summary("목표 분석 생성")
                        .description("완료된 목표에 대해 AI 분석을 생성합니다.")
                        .pathParameters(parameterWithName("id").description("분석할 목표 ID"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.STRING)
                                .description("분석 완료 메시지"))
                        .build())));
  }
}
