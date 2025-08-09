package com.growit.app.retrospect.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.usecase.CheckRetrospectExistsByPlanIdUseCase;
import com.growit.app.retrospect.usecase.CreateRetrospectUseCase;
import com.growit.app.retrospect.usecase.GetRetrospectByFilterUseCase;
import com.growit.app.retrospect.usecase.GetRetrospectUseCase;
import com.growit.app.retrospect.usecase.GetRetrospectsByGoalIdUseCase;
import com.growit.app.retrospect.usecase.UpdateRetrospectUseCase;
import com.growit.app.retrospect.usecase.dto.RetrospectWithPlan;
import java.util.List;
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
class RetrospectControllerTest {
  private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private CreateRetrospectUseCase createRetrospectUseCase;
  @MockitoBean private UpdateRetrospectUseCase updateRetrospectUseCase;
  @MockitoBean private GetRetrospectUseCase getRetrospectUseCase;
  @MockitoBean private GetRetrospectByFilterUseCase getRetrospectByFilterUseCase;
  @MockitoBean private GetRetrospectsByGoalIdUseCase getRetrospectsByGoalIdUseCase;
  @MockitoBean private CheckRetrospectExistsByPlanIdUseCase checkRetrospectExistsByPlanIdUseCase;

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
  void createRetrospect() throws Exception {
    CreateRetrospectRequest body = RetrospectFixture.defaultCreateRetrospectRequest();
    given(createRetrospectUseCase.execute(any())).willReturn("retrospect-id");
    mockMvc
        .perform(
            post("/retrospects")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andDo(
            document(
                "create-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Retrospects")
                        .summary("회고 생성")
                        .requestFields(
                            fieldWithPath("goalId")
                                .type(JsonFieldType.STRING)
                                .description("목표 아이디"),
                            fieldWithPath("planId")
                                .type(JsonFieldType.STRING)
                                .description("계획 아이디"),
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("회고 내용"))
                        .responseFields(fieldWithPath("data.id").type(STRING).description("회고 ID"))
                        .build())));
  }

  @Test
  void updateRetrospect() throws Exception {
    UpdateRetrospectRequest body = RetrospectFixture.defaultUpdateRetrospectRequest();
    mockMvc
        .perform(
            put("/retrospects/{id}", "retrospect-id")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "update-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Retrospects")
                        .summary("회고 수정")
                        .pathParameters(parameterWithName("id").description("회고 ID"))
                        .requestFields(
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("회고 내용"))
                        .build())));
  }

  @Test
  void getRetrospect() throws Exception {
    RetrospectWithPlan retrospectWithPlan = RetrospectFixture.defaultRetrospectWithPlan();
    given(getRetrospectUseCase.execute(any())).willReturn(retrospectWithPlan);

    mockMvc
        .perform(
            get("/retrospects/{id}", retrospectWithPlan.getRetrospect().getId())
                .header("Authorization", "Bearer mock-jwt-token")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Retrospects")
                        .summary("회고 단건 조회")
                        .pathParameters(parameterWithName("id").description("회고 ID"))
                        .responseFields(
                            fieldWithPath("data.id").description("회고 ID"),
                            fieldWithPath("data.goalId").description("목표 ID"),
                            fieldWithPath("data.plan.id").description("계획 ID"),
                            fieldWithPath("data.plan.weekOfMonth").description("계획 주차"),
                            fieldWithPath("data.plan.content").description("계획 내용"),
                            fieldWithPath("data.content").description("회고 내용"))
                        .build())));
  }

  @Test
  void getRetrospectByGoalIdAndPlanId() throws Exception {
    RetrospectWithPlan retrospectWithPlan = RetrospectFixture.defaultRetrospectWithPlan();
    given(getRetrospectByFilterUseCase.execute(any())).willReturn(retrospectWithPlan);

    mockMvc
        .perform(
            get("/retrospects")
                .param("goalId", retrospectWithPlan.getRetrospect().getGoalId())
                .param("planId", retrospectWithPlan.getRetrospect().getPlanId())
                .header("Authorization", "Bearer mock-jwt-token")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-retrospect-by-filter",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Retrospects")
                        .summary("회고 단건 조회 by goalId planId")
                        .queryParameters(
                            parameterWithName("goalId").description("목표 ID"),
                            parameterWithName("planId").description("계획 ID"))
                        .responseFields(
                            fieldWithPath("data.id").description("회고 ID"),
                            fieldWithPath("data.goalId").description("목표 ID"),
                            fieldWithPath("data.plan.id").description("계획 ID"),
                            fieldWithPath("data.plan.weekOfMonth").description("계획 주차"),
                            fieldWithPath("data.plan.content").description("계획 내용"),
                            fieldWithPath("data.content").description("회고 내용"))
                        .build())));
  }

  @Test
  void checkRetrospect() throws Exception {
    given(checkRetrospectExistsByPlanIdUseCase.execute(any())).willReturn(true);
    mockMvc
        .perform(
            get("/retrospects/exists")
                .param("goalId", "goal-123")
                .param("planId", "plan-456")
                .header("Authorization", "Bearer mock-jwt-token")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "check-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Retrospects")
                        .summary("회고 존재 여부 확인")
                        .queryParameters(
                            parameterWithName("goalId").description("목표 ID"),
                            parameterWithName("planId").description("계획 ID"))
                        .responseFields(
                            fieldWithPath("data.isExist").description("회고 존재 여부 (true/false)"))
                        .build())));
  }

  @Test
  void getRetrospectsByGoalId() throws Exception {
    RetrospectWithPlan retrospectWithPlan1 = RetrospectFixture.defaultRetrospectWithPlan();
    RetrospectWithPlan retrospectWithPlan2 = RetrospectFixture.defaultRetrospectWithPlan();
    given(getRetrospectsByGoalIdUseCase.execute(any(), any()))
        .willReturn(List.of(retrospectWithPlan1, retrospectWithPlan2));

    mockMvc
        .perform(
            get("/retrospects")
                .param("goalId", "goal-123")
                .header("Authorization", "Bearer mock-jwt-token")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-retrospects-by-goal-id",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Retrospects")
                        .summary("목표별 회고 목록 조회")
                        .queryParameters(parameterWithName("goalId").description("목표 ID"))
                        .responseFields(
                            fieldWithPath("data[]").description("회고 목록"),
                            fieldWithPath("data[].id").type(STRING).description("회고 ID"),
                            fieldWithPath("data[].goalId").type(STRING).description("목표 ID"),
                            fieldWithPath("data[].plan").description("계획 정보"),
                            fieldWithPath("data[].plan.id").type(STRING).description("계획 ID"),
                            fieldWithPath("data[].plan.weekOfMonth").description("주차"),
                            fieldWithPath("data[].plan.content").type(STRING).description("계획 내용"),
                            fieldWithPath("data[].content").type(STRING).description("회고 내용"))
                        .build())));
  }
}
