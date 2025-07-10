package com.growit.app.todo.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.BOOLEAN;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.FakeToDoRepositoryConfig;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.controller.dto.request.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.request.CreateToDoRequest;
import com.growit.app.todo.controller.dto.request.UpdateToDoRequest;
import com.growit.app.todo.controller.dto.response.WeeklyTodosResponse;
import com.growit.app.todo.controller.mapper.ToDoResponseMapper;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.vo.ToDoStatus;
import com.growit.app.todo.usecase.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.reflect.FieldUtils;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@Import(FakeToDoRepositoryConfig.class)
class ToDoControllerTest {
  private MockMvc mockMvc;

  @MockitoBean private CreateToDoUseCase createToDoUseCase;
  @MockitoBean private UpdateToDoUseCase updateToDoUseCase;
  @MockitoBean private CompletedStatusChangeToDoUseCase statusChangeToDoUseCase;
  @MockitoBean private GetToDoUseCase getToDoUseCase;
  @MockitoBean private DeleteToDoUseCase deleteToDoUseCase;
  @MockitoBean private GetWeeklyTodosUseCase getWeeklyTodosUseCase;
  @MockitoBean private ToDoResponseMapper toDoResponseMapper;
  @MockitoBean private GetTodayMissionUseCase getTodayMissionUseCase;
  @MockitoBean
  private GetContributionUseCase getContributionUseCase;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private ToDoRepository toDoRepository;


  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    TestSecurityUtil.setMockUser();
    if (toDoRepository instanceof FakeToDoRepository fake) {
      fake.clear();
    }
  }

  @Test
  void createToDo() throws Exception {
    given(createToDoUseCase.execute(any())).willReturn("todo-1");

    CreateToDoRequest body = ToDoFixture.defaultCreateToDoRequest();
    mockMvc
        .perform(
            post("/todos")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andDo(
            document(
                "create-todo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("할 일(TODO) 생성")
                        .requestFields(
                            fieldWithPath("goalId").type(STRING).description("목표 ID"),
                            fieldWithPath("planId").type(STRING).description("계획 ID"),
                            fieldWithPath("content")
                                .type(STRING)
                                .description("할 일 내용 (5자 이상 30자 미만)"),
                            fieldWithPath("date").type(STRING).description("할 일 날짜 (yyyy-MM-dd)"))
                        .responseFields(
                            fieldWithPath("data.id").type(STRING).description("생성된 TODO ID"))
                        .build())));
  }

  @Test
  void updateToDo() throws Exception {
    String toDoId = "todo-1";
    willDoNothing().given(updateToDoUseCase).execute(any());

    UpdateToDoRequest body = new UpdateToDoRequest(java.time.LocalDate.now(), "수정된 내용");

    mockMvc
        .perform(
            put("/todos/{id}", toDoId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "update-todo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("할 일(TODO) 수정")
                        .pathParameters(parameterWithName("id").description("수정할 TODO ID"))
                        .requestFields(
                            fieldWithPath("content")
                                .type(STRING)
                                .description("수정할 할 일 내용 (5자 이상 30자 미만)"),
                            fieldWithPath("date").type(STRING).description("할 일 날짜 (yyyy-MM-dd)"))
                        .responseFields(
                            fieldWithPath("data").type(STRING).description("업데이트 결과 메시지"))
                        .build())));
  }

  @Test
  void statusChangeToDo() throws Exception {
    String toDoId = "todo-1";
    willDoNothing().given(statusChangeToDoUseCase).execute(any());

    CompletedStatusChangeRequest request = new CompletedStatusChangeRequest();
    FieldUtils.writeField(request, "completed", true, true);

    mockMvc
        .perform(
            patch("/todos/{id}", toDoId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "status-change-todo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("할 일(TODO) 완료 상태 변경")
                        .description("할 일의 완료 상태를 변경한다.")
                        .pathParameters(parameterWithName("id").description("상태를 변경할 TODO ID"))
                        .requestFields(
                            fieldWithPath("isCompleted").type("Boolean").description("완료 여부"))
                        .responseFields(
                            fieldWithPath("data").type("String").description("변경 결과 메시지"))
                        .build())));
  }

  @Test
  void deletedTodo() throws Exception {
    String toDoId = "todo-1";
    willDoNothing().given(deleteToDoUseCase).execute(any());

    mockMvc
        .perform(
            delete("/todos/{id}", toDoId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "delete-todo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("할 일(TODO) 삭제")
                        .description("할 일을 삭제한다.")
                        .pathParameters(parameterWithName("id").description("상태를 변경할 TODO ID"))
                        .responseFields(fieldWithPath("data").type("String").description("결과 메시지"))
                        .build())));
  }

  @Test
  void getToDo() throws Exception {
    given(getToDoUseCase.execute(any())).willReturn(ToDoFixture.defaultToDo());
    mockMvc
        .perform(
            get("/todos/{id}", "todoId")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-todo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("할 일(TODO) 조회")
                        .pathParameters(parameterWithName("id").description("수정할 TODO ID"))
                        .responseFields(
                            fieldWithPath("data.id").type(STRING).description("할일 ID"),
                            fieldWithPath("data.goalId").type(STRING).description("목표 ID"),
                            fieldWithPath("data.planId").type(STRING).description("계획 ID"),
                            fieldWithPath("data.content")
                                .type(STRING)
                                .description("할 일 내용 (5자 이상 30자 미만)"),
                            fieldWithPath("data.date")
                                .type(STRING)
                                .description("할 일 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data.isCompleted").type(BOOLEAN).description("완료 여부"))
                        .build())));
  }

  @Test
  void getWeeklyTodos() throws Exception {
    // given
    String userId = "user-1";
    String goalId = "goal-123";
    String planId = "plan-456";

    // 도메인 객체 반환 (요일별 Group)
    Map<DayOfWeek, List<ToDo>> grouped =
        Map.of(
            DayOfWeek.MONDAY, List.of(ToDoFixture.defaultToDo()),
            DayOfWeek.TUESDAY, List.of());

    WeeklyTodosResponse mondayResponse =
        WeeklyTodosResponse.builder()
            .id("todoId")
            .goalId(goalId)
            .planId(planId)
            .content("목표")
            .date(LocalDate.now().toString())
            .completed(true)
            .build();

    Map<String, List<WeeklyTodosResponse>> mapped =
        ToDoFixture.weeklyTodosMapWith("MONDAY", List.of(mondayResponse));

    given(getWeeklyTodosUseCase.execute(goalId, planId, userId)).willReturn(grouped);
    given(toDoResponseMapper.toWeeklyPlanResponse(grouped)).willReturn(mapped);

    // when & then
    mockMvc
        .perform(
            get("/todos")
                .header("Authorization", "Bearer mock-jwt-token")
                .param("goalId", goalId)
                .param("planId", planId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-weekly-plan",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("주간 할 일(Weekly Plan) 조회")
                        .description("특정 목표/플랜에 대한 요일별 할 일을 조회합니다.")
                        .queryParameters(
                            parameterWithName("goalId").description("목표 ID"),
                            parameterWithName("planId").description("계획 ID"))
                        .responseFields(
                            fieldWithPath("data.MONDAY[].id").type(STRING).description("TODO ID"),
                            fieldWithPath("data.MONDAY[].goalId").type(STRING).description("목표 ID"),
                            fieldWithPath("data.MONDAY[].planId").type(STRING).description("계획 ID"),
                            fieldWithPath("data.MONDAY[].content").type(STRING).description("내용"),
                            fieldWithPath("data.MONDAY[].date").type(STRING).description("할 일 날짜"),
                            fieldWithPath("data.MONDAY[].isCompleted")
                                .type(BOOLEAN)
                                .description("완료 여부"),
                            fieldWithPath("data.TUESDAY").description("화요일 할 일 리스트(없을 수도 있음)"),
                            fieldWithPath("data.WEDNESDAY").description("수요일 할 일 리스트(없을 수도 있음)"),
                            fieldWithPath("data.THURSDAY").description("목요일 할 일 리스트(없을 수도 있음)"),
                            fieldWithPath("data.FRIDAY").description("금요일 할 일 리스트(없을 수도 있음)"),
                            fieldWithPath("data.SATURDAY").description("토요일 할 일 리스트(없을 수도 있음)"),
                            fieldWithPath("data.SUNDAY").description("일요일 할 일 리스트(없을 수도 있음)"))
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
            get("/todos")
                .header("Authorization", "Bearer mock-jwt-token")
                .param("date", LocalDate.now().toString())
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

  @Test
  void getContribution() throws Exception {
    // given
    String goalId = "goal-123";
    List<ToDoStatus> statusList = List.of(
      ToDoStatus.COMPLETED, ToDoStatus.NOT_STARTED, ToDoStatus.IN_PROGRESS, ToDoStatus.NONE
    );
    given(getContributionUseCase.execute(anyString(), eq(goalId))).willReturn(statusList);

    // when & then
    mockMvc.perform(
        get("/todos")
          .header("Authorization", "Bearer mock-jwt-token")
          .param("goalId", goalId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(
        document(
          "get-contribution",
          preprocessRequest(prettyPrint()),
          preprocessResponse(prettyPrint()),
          resource(
            new ResourceSnippetParametersBuilder()
              .tag("Todos")
              .summary("목표별 28일 기여도 리스트 조회")
              .description("특정 목표(goalId)에 대한 28일간의 기여도(상태) 리스트를 반환합니다.")
              .queryParameters(
                parameterWithName("goalId").description("목표 ID")
              )
              .responseFields(
                fieldWithPath("data[]").type("String")
                  .description("28일간 각 날짜별 ToDoStatus(예: COMPLETED, NOT_STARTED, IN_PROGRESS, NONE)")
              )
              .build()
          )
        )
      );
  }
}
