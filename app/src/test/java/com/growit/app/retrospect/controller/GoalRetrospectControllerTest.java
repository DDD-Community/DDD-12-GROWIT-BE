package com.growit.app.retrospect.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.goalretrospect.GoalRetrospectFixture;
import com.growit.app.retrospect.controller.goalretrospect.dto.request.CreateGoalRetrospectRequest;
import com.growit.app.retrospect.controller.goalretrospect.dto.request.UpdateGoalRetrospectRequest;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.usecase.goalretrospect.CreateGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.GetGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.UpdateGoalRetrospectUseCase;
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

  @MockitoBean private CreateGoalRetrospectUseCase createGoalRetrospectUseCase;
  @MockitoBean private UpdateGoalRetrospectUseCase updateGoalRetrospectUseCase;
  @MockitoBean private GetGoalRetrospectUseCase getGoalRetrospectUseCase;

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
                        .tag("Goal Retrospects")
                        .summary("목표회고 단건 조회")
                        .pathParameters(parameterWithName("id").description("목표회고 ID"))
                        .responseFields(
                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                            fieldWithPath("data.id")
                                .type(JsonFieldType.STRING)
                                .description("목표회고 ID"),
                            fieldWithPath("data.goalId")
                                .type(JsonFieldType.STRING)
                                .description("목표 ID"),
                            fieldWithPath("data.todoCompletedRate")
                                .type(JsonFieldType.NUMBER)
                                .description("할일 완료율"),
                            fieldWithPath("data.analysis")
                                .type(JsonFieldType.OBJECT)
                                .description("분석 결과"),
                            fieldWithPath("data.analysis.summary")
                                .type(JsonFieldType.STRING)
                                .description("요약"),
                            fieldWithPath("data.analysis.advice")
                                .type(JsonFieldType.STRING)
                                .description("조언"),
                            fieldWithPath("data.content")
                                .type(JsonFieldType.STRING)
                                .description("회고 내용"))
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
                        .tag("Goal Retrospects")
                        .summary("목표 회고 생성")
                        .requestFields(
                            fieldWithPath("goalId")
                                .type(JsonFieldType.STRING)
                                .description("목표 아이디"))
                        .responseFields(
                            fieldWithPath("data.id")
                                .type(JsonFieldType.STRING)
                                .description("목표 회고 아이디"))
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
