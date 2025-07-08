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
import com.growit.app.fake.goal.PlanFixture;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectWithPlan;
import com.growit.app.retrospect.usecase.CreateRetrospectUseCase;
import com.growit.app.retrospect.usecase.GetRetrospectUseCase;
import com.growit.app.retrospect.usecase.UpdateRetrospectUseCase;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.usecase.GetTodayMissionUseCase;
import java.time.LocalDate;
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
  @MockitoBean private GetTodayMissionUseCase getTodayMissionUseCase;

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
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    String planId = retrospect.getPlanId();
    Plan plan = PlanFixture.customPlan(planId, null, null, null, null);

    RetrospectWithPlan retrospectWithPlan = new RetrospectWithPlan(retrospect, plan);

    given(getRetrospectUseCase.execute(any())).willReturn(retrospectWithPlan);

    mockMvc
        .perform(
            get("/retrospects/{id}", retrospect.getId())
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
  void getTodayMission() throws Exception {
    // given
    List<ToDo> todoList =
        List.of(
            ToDoFixture.customToDo("id", "user-1", LocalDate.now(), "planId", "goalId"),
            ToDoFixture.customToDo("id2", "user-1", LocalDate.now(), "planId", "goalId"));
    given(getTodayMissionUseCase.execute(any(), any())).willReturn(todoList);

    // when & then
    mockMvc
        .perform(
            get("/todos/today-mission/date")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-today-mission",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("오늘 미션 조회")
                        .description("오늘 날짜의 미완료 ToDo 리스트를 조회합니다.")
                        .responseFields(
                            fieldWithPath("data[].id").type(STRING).description("TODO ID"),
                            fieldWithPath("data[].goalId").type(STRING).description("목표 ID"),
                            fieldWithPath("data[].planId").type(STRING).description("계획 ID"),
                            fieldWithPath("data[].date").type(STRING).description("할 일 날짜"),
                            fieldWithPath("data[].content").type(STRING).description("내용"),
                            fieldWithPath("data[].isCompleted")
                                .type("Boolean")
                                .description("완료 여부"))
                        .build())));
  }
}
