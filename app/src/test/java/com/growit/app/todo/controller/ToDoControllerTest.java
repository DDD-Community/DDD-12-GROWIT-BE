package com.growit.app.todo.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.controller.dto.request.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.request.CreateToDoRequest;
import com.growit.app.todo.controller.dto.request.UpdateToDoRequest;
import com.growit.app.todo.controller.dto.response.GoalDto;
import com.growit.app.todo.controller.dto.response.RoutineDto;
import com.growit.app.todo.controller.dto.response.ToDoResponse;
import com.growit.app.todo.controller.dto.response.ToDoWithGoalResponse;
import com.growit.app.todo.controller.dto.response.TodoCountByDateResponse;
import com.growit.app.todo.controller.dto.response.TodoDto;
import com.growit.app.todo.controller.mapper.ToDoRequestMapper;
import com.growit.app.todo.controller.mapper.ToDoResponseMapper;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.CompletedStatusChangeCommand;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.vo.RepeatType;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineDeleteType;
import com.growit.app.todo.domain.vo.RoutineDuration;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import com.growit.app.todo.usecase.*;
import com.growit.app.todo.usecase.dto.ToDoWithGoalDto;
import com.growit.app.todo.usecase.dto.TodoCountByDateDto;
import java.time.LocalDate;
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
class ToDoControllerTest {
  private MockMvc mockMvc;

  @MockitoBean private CreateToDoUseCase createToDoUseCase;
  @MockitoBean private UpdateToDoUseCase updateToDoUseCase;
  @MockitoBean private CompletedStatusChangeToDoUseCase statusChangeToDoUseCase;
  @MockitoBean private GetToDoUseCase getToDoUseCase;
  @MockitoBean private DeleteToDoUseCase deleteToDoUseCase;
  @MockitoBean private GetTodosWithGoalByDateUseCase getTodosWithGoalByDateUseCase;
  @MockitoBean private GetTodoCountByGoalInDateRangeUseCase getTodoCountByGoalInDateRangeUseCase;
  @MockitoBean private ToDoRequestMapper toDoRequestMapper;
  @MockitoBean private ToDoResponseMapper toDoResponseMapper;

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
  void createToDo() throws Exception {
    // given
    RoutineDto routineDto =
        RoutineDto.builder()
            .duration(
                RoutineDto.DurationDto.builder()
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2024, 1, 7))
                    .build())
            .repeatType("DAILY")
            .build();

    CreateToDoRequest request =
        new CreateToDoRequest("goal-1", LocalDate.now(), "할 일 내용", false, routineDto);

    Routine domainRoutine =
        Routine.of(
            RoutineDuration.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 7)),
            RepeatType.DAILY,
            null);

    CreateToDoCommand command =
        new CreateToDoCommand("user-1", "goal-1", "할 일 내용", LocalDate.now(), false, domainRoutine);
    ToDoResult result = new ToDoResult("todo-1");
    ToDoResponse response = new ToDoResponse("todo-1");

    given(toDoRequestMapper.toCreateCommand(any(String.class), any(CreateToDoRequest.class)))
        .willReturn(command);
    given(createToDoUseCase.execute(any(CreateToDoCommand.class))).willReturn(result);
    given(toDoResponseMapper.toToDoResponse(any(ToDoResult.class))).willReturn(response);

    // when & then
    mockMvc
        .perform(
            post("/todos")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "create-todo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("ToDo 생성")
                        .description("새로운 ToDo를 생성합니다.")
                        .requestFields(
                            fieldWithPath("goalId").type(JsonFieldType.STRING).description("목표 ID"),
                            fieldWithPath("date")
                                .type(JsonFieldType.STRING)
                                .description("ToDo 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("ToDo 내용 (1-30자)"),
                            fieldWithPath("isImportant")
                                .type(JsonFieldType.BOOLEAN)
                                .description("중요도 여부"),
                            fieldWithPath("routine")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("루틴 정보 (선택사항)"),
                            fieldWithPath("routine.duration")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("루틴 기간 정보"),
                            fieldWithPath("routine.duration.startDate")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 시작 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("routine.duration.endDate")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 종료 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("routine.repeatType")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description(
                                    "반복 유형 (DAILY: 매일, WEEKLY: 매주, BIWEEKLY: 격주, MONTHLY: 매월)"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.OBJECT)
                                .description("생성된 ToDo 정보"),
                            fieldWithPath("data.id")
                                .type(JsonFieldType.STRING)
                                .description("생성된 ToDo ID"))
                        .build())));
  }

  @Test
  void updateToDo() throws Exception {
    // given
    String todoId = "todo-123";
    RoutineDto.DurationDto durationDto =
        RoutineDto.DurationDto.builder()
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(7))
            .build();
    RoutineDto routineDto = RoutineDto.builder().duration(durationDto).repeatType("DAILY").build();

    RoutineDuration duration = RoutineDuration.of(LocalDate.now(), LocalDate.now().plusDays(7));
    Routine routine = Routine.of(duration, RepeatType.DAILY, null);
    UpdateToDoRequest request =
        new UpdateToDoRequest(
            "goal-1", LocalDate.now(), "수정된 할 일 내용", true, routineDto, RoutineUpdateType.ALL);
    UpdateToDoCommand command =
        new UpdateToDoCommand(
            todoId,
            "user-1",
            "goal-1",
            "수정된 할 일 내용",
            LocalDate.now(),
            true,
            routine,
            RoutineUpdateType.ALL);

    ToDoResult result = new ToDoResult("todo-123");

    given(
            toDoRequestMapper.toUpdateCommand(
                eq(todoId), any(String.class), any(UpdateToDoRequest.class)))
        .willReturn(command);
    given(updateToDoUseCase.execute(any(UpdateToDoCommand.class))).willReturn(result);

    // when & then
    mockMvc
        .perform(
            put("/todos/{id}", todoId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "update-todo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("ToDo 수정")
                        .description("기존 ToDo 정보를 수정합니다.")
                        .pathParameters(parameterWithName("id").description("ToDo ID"))
                        .requestFields(
                            fieldWithPath("goalId")
                                .type(JsonFieldType.STRING)
                                .description("수정할 목표 ID"),
                            fieldWithPath("date")
                                .type(JsonFieldType.STRING)
                                .description("수정할 ToDo 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("수정할 ToDo 내용 (1-30자)"),
                            fieldWithPath("isImportant")
                                .type(JsonFieldType.BOOLEAN)
                                .description("수정할 중요도 여부"),
                            fieldWithPath("routine")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("수정할 루틴 정보"),
                            fieldWithPath("routine.duration")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("루틴 기간"),
                            fieldWithPath("routine.duration.startDate")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 시작일 (yyyy-MM-dd)"),
                            fieldWithPath("routine.duration.endDate")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 종료일 (yyyy-MM-dd)"),
                            fieldWithPath("routine.repeatType")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("반복 타입 (DAILY, WEEKLY, BIWEEKLY, MONTHLY)"),
                            fieldWithPath("routineUpdateType")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 수정 타입 (SINGLE, FROM_DATE, ALL)"))
                        .build())));
  }

  @Test
  void updateToDoWithRoutine() throws Exception {
    // given
    String todoId = "todo-123";

    RoutineDto routineDto =
        RoutineDto.builder()
            .duration(
                RoutineDto.DurationDto.builder()
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2024, 1, 7))
                    .build())
            .repeatType("DAILY")
            .build();

    UpdateToDoRequest request =
        new UpdateToDoRequest(
            "goal-123",
            LocalDate.of(2024, 1, 1),
            "Updated routine task",
            true,
            routineDto,
            RoutineUpdateType.FROM_DATE);

    UpdateToDoCommand command =
        new UpdateToDoCommand(
            todoId,
            "user-123",
            "goal-123",
            "Updated routine task",
            LocalDate.of(2024, 1, 1),
            true,
            null,
            RoutineUpdateType.FROM_DATE);
    ToDoResult result = new ToDoResult("todo-123");

    given(
            toDoRequestMapper.toUpdateCommand(
                eq(todoId), any(String.class), any(UpdateToDoRequest.class)))
        .willReturn(command);
    given(updateToDoUseCase.execute(any(UpdateToDoCommand.class))).willReturn(result);
    given(toDoResponseMapper.toToDoResponse(any(ToDoResult.class)))
        .willReturn(new ToDoResponse("todo-123"));

    // when & then
    mockMvc
        .perform(
            put("/todos/{id}", todoId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void deleteToDoWithRoutineOption() throws Exception {
    // given
    String todoId = "todo-123";
    RoutineDeleteType routineDeleteType = RoutineDeleteType.ALL;

    willDoNothing().given(deleteToDoUseCase).execute(any());

    // when & then
    mockMvc
        .perform(
            delete("/todos/{id}", todoId)
                .header("Authorization", "Bearer mock-jwt-token")
                .param("routineDeleteType", routineDeleteType.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value("삭제가 완료되었습니다."))
        .andDo(
            document(
                "delete-todo-with-routine",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("루틴 ToDo 삭제")
                        .description("루틴 옵션과 함께 ToDo를 삭제합니다.")
                        .pathParameters(parameterWithName("id").description("ToDo ID"))
                        .queryParameters(
                            parameterWithName("routineDeleteType")
                                .optional()
                                .description("루틴 삭제 타입 (SINGLE, FROM_DATE, ALL)"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.STRING)
                                .description("삭제 완료 메시지"))
                        .build())));
  }

  @Test
  void changeStatus() throws Exception {
    // given
    String todoId = "todo-123";
    CompletedStatusChangeRequest request = new CompletedStatusChangeRequest();

    given(
            toDoRequestMapper.toCompletedStatusChangeCommand(
                eq(todoId), any(String.class), any(CompletedStatusChangeRequest.class)))
        .willReturn(null);
    willDoNothing().given(statusChangeToDoUseCase).execute(any(CompletedStatusChangeCommand.class));

    // when & then
    mockMvc
        .perform(
            patch("/todos/{id}", todoId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "change-todo-status",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("ToDo 상태 변경")
                        .description("ToDo의 완료 상태나 중요도를 변경합니다.")
                        .pathParameters(parameterWithName("id").description("ToDo ID"))
                        .requestFields(
                            fieldWithPath("isCompleted")
                                .type(JsonFieldType.BOOLEAN)
                                .optional()
                                .description("완료 여부 (선택사항)"),
                            fieldWithPath("isImportant")
                                .type(JsonFieldType.BOOLEAN)
                                .optional()
                                .description("중요도 여부 (선택사항)"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.STRING)
                                .description("상태 변경 완료 메시지"))
                        .build())));
  }

  @Test
  void getTodosByDate() throws Exception {
    // given
    String date = "2024-01-01";

    // Create mock Goal
    Goal mockGoal = GoalFixture.customGoal("goal-1", "테스트 목표", null);

    ToDo todo1 = ToDoFixture.customToDo("todo-1", "user-1", LocalDate.of(2024, 1, 1), "goal-1");
    ToDo todo2 = ToDoFixture.customToDo("todo-2", "user-1", LocalDate.of(2024, 1, 1), "goal-1");

    // Create ToDoWithGoalDto list
    List<ToDoWithGoalDto> todoList =
        List.of(new ToDoWithGoalDto(todo1, mockGoal), new ToDoWithGoalDto(todo2, mockGoal));

    // Create response list
    List<ToDoWithGoalResponse> responseList =
        List.of(
            ToDoWithGoalResponse.builder()
                .todo(
                    TodoDto.builder()
                        .id("todo-1")
                        .goalId("goal-1")
                        .date("2024-01-01")
                        .content("테스트 할 일입니다.")
                        .important(false)
                        .completed(false)
                        .routine(
                            RoutineDto.builder()
                                .duration(
                                    RoutineDto.DurationDto.builder()
                                        .startDate(LocalDate.of(2024, 1, 1))
                                        .endDate(LocalDate.of(2024, 1, 7))
                                        .build())
                                .repeatType("DAILY")
                                .build())
                        .build())
                .goal(GoalDto.builder().id("goal-1").name("테스트 목표").build())
                .build(),
            ToDoWithGoalResponse.builder()
                .todo(
                    TodoDto.builder()
                        .id("todo-2")
                        .goalId("goal-1")
                        .date("2024-01-01")
                        .content("테스트 할 일입니다.")
                        .important(false)
                        .completed(false)
                        .routine(null)
                        .build())
                .goal(GoalDto.builder().id("goal-1").name("테스트 목표").build())
                .build());

    given(toDoRequestMapper.toGetDateQueryFilter(any(String.class), eq(date))).willReturn(null);
    given(getTodosWithGoalByDateUseCase.execute(any())).willReturn(todoList);
    given(toDoResponseMapper.toToDoWithGoalResponseList(any())).willReturn(responseList);

    // when & then
    mockMvc
        .perform(get("/todos").param("date", date).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-todos-by-date",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("날짜별 ToDo 조회")
                        .description("특정 날짜의 ToDo 목록을 목표 정보와 함께 조회합니다.")
                        .queryParameters(
                            parameterWithName("date").description("조회할 날짜 (yyyy-MM-dd)"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.ARRAY)
                                .description("ToDo 목록 데이터 배열"),
                            fieldWithPath("data[].todo")
                                .type(JsonFieldType.OBJECT)
                                .description("ToDo 정보"),
                            fieldWithPath("data[].todo.id")
                                .type(JsonFieldType.STRING)
                                .description("ToDo ID"),
                            fieldWithPath("data[].todo.goalId")
                                .type(JsonFieldType.STRING)
                                .description("목표 ID"),
                            fieldWithPath("data[].todo.date")
                                .type(JsonFieldType.STRING)
                                .description("ToDo 날짜"),
                            fieldWithPath("data[].todo.content")
                                .type(JsonFieldType.STRING)
                                .description("ToDo 내용"),
                            fieldWithPath("data[].todo.isImportant")
                                .type(JsonFieldType.BOOLEAN)
                                .description("중요도 여부"),
                            fieldWithPath("data[].todo.isCompleted")
                                .type(JsonFieldType.BOOLEAN)
                                .description("완료 여부"),
                            fieldWithPath("data[].todo.routine")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("루틴 정보 (null 가능)"),
                            fieldWithPath("data[].todo.routine.duration")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("루틴 기간 정보"),
                            fieldWithPath("data[].todo.routine.duration.startDate")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 시작 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data[].todo.routine.duration.endDate")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 종료 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data[].todo.routine.repeatType")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description(
                                    "반복 유형 (DAILY: 매일, WEEKLY: 매주, BIWEEKLY: 격주, MONTHLY: 매월)"),
                            fieldWithPath("data[].goal")
                                .type(JsonFieldType.OBJECT)
                                .description("목표 정보"),
                            fieldWithPath("data[].goal.id")
                                .type(JsonFieldType.STRING)
                                .description("목표 ID"),
                            fieldWithPath("data[].goal.name")
                                .type(JsonFieldType.STRING)
                                .description("목표 이름"))
                        .build())));
  }

  @Test
  void getToDoById() throws Exception {
    // given
    String todoId = "todo-123";
    ToDo todo = ToDoFixture.defaultToDo();
    TodoDto todoDto =
        TodoDto.builder()
            .id("todo-123")
            .goalId("goal-1")
            .date("2024-01-01")
            .content("테스트 할 일입니다.")
            .important(false)
            .completed(false)
            .routine(null)
            .build();

    given(toDoRequestMapper.toGetQuery(eq(todoId), any(String.class))).willReturn(null);
    given(getToDoUseCase.execute(any())).willReturn(todo);
    given(toDoResponseMapper.toTodoDto(any(ToDo.class))).willReturn(todoDto);

    // when & then
    mockMvc
        .perform(get("/todos/{id}", todoId).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-todo-by-id",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("특정 ToDo 조회")
                        .description("특정 ToDo의 상세 정보를 조회합니다.")
                        .pathParameters(parameterWithName("id").description("ToDo ID"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.OBJECT)
                                .description("ToDo 상세 정보"),
                            fieldWithPath("data.id")
                                .type(JsonFieldType.STRING)
                                .description("ToDo ID"),
                            fieldWithPath("data.goalId")
                                .type(JsonFieldType.STRING)
                                .description("목표 ID"),
                            fieldWithPath("data.content")
                                .type(JsonFieldType.STRING)
                                .description("ToDo 내용"),
                            fieldWithPath("data.date")
                                .type(JsonFieldType.STRING)
                                .description("ToDo 날짜"),
                            fieldWithPath("data.isCompleted")
                                .type(JsonFieldType.BOOLEAN)
                                .description("완료 여부"),
                            fieldWithPath("data.isImportant")
                                .type(JsonFieldType.BOOLEAN)
                                .description("중요도 여부"),
                            fieldWithPath("data.routine")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("루틴 정보 (null 가능)"),
                            fieldWithPath("data.routine.duration")
                                .type(JsonFieldType.OBJECT)
                                .optional()
                                .description("루틴 기간 정보"),
                            fieldWithPath("data.routine.duration.startDate")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 시작 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data.routine.duration.endDate")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("루틴 종료 날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data.routine.repeatType")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description(
                                    "반복 유형 (DAILY: 매일, WEEKLY: 매주, BIWEEKLY: 격주, MONTHLY: 매월)"))
                        .build())));
  }

  @Test
  void deleteToDo() throws Exception {
    // given
    String todoId = "todo-123";

    given(toDoRequestMapper.toDeleteCommand(eq(todoId), any(String.class))).willReturn(null);
    willDoNothing().given(deleteToDoUseCase).execute(any());

    // when & then
    mockMvc
        .perform(delete("/todos/{id}", todoId).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "delete-todo",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("ToDo 삭제")
                        .description("특정 ToDo를 삭제합니다.")
                        .pathParameters(parameterWithName("id").description("삭제할 ToDo ID"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.STRING)
                                .description("삭제 완료 메시지"))
                        .build())));
  }

  @Test
  void getTodoCountByDateRange() throws Exception {
    // given
    String from = "2024-01-01";
    String to = "2024-01-07";
    List<TodoCountByDateDto> todoCountList =
        List.of(
            new TodoCountByDateDto(
                LocalDate.of(2024, 1, 1),
                List.of(
                    new TodoCountByDateDto.GoalTodoCount("goal-1", 3),
                    new TodoCountByDateDto.GoalTodoCount("goal-2", 2))),
            new TodoCountByDateDto(
                LocalDate.of(2024, 1, 2),
                List.of(
                    new TodoCountByDateDto.GoalTodoCount("goal-1", 1),
                    new TodoCountByDateDto.GoalTodoCount("goal-2", 4))));
    List<TodoCountByDateResponse> responseList =
        List.of(
            TodoCountByDateResponse.builder()
                .date("2024-01-01")
                .goals(
                    List.of(
                        new TodoCountByDateResponse.GoalTodoCount("goal-1", 3),
                        new TodoCountByDateResponse.GoalTodoCount("goal-2", 2)))
                .build(),
            TodoCountByDateResponse.builder()
                .date("2024-01-02")
                .goals(
                    List.of(
                        new TodoCountByDateResponse.GoalTodoCount("goal-1", 1),
                        new TodoCountByDateResponse.GoalTodoCount("goal-2", 4)))
                .build());

    given(toDoRequestMapper.toGetDateRangeQueryFilter(any(String.class), eq(from), eq(to)))
        .willReturn(null);
    given(getTodoCountByGoalInDateRangeUseCase.execute(any())).willReturn(todoCountList);
    given(toDoResponseMapper.toTodoCountByDateResponseList(any())).willReturn(responseList);

    // when & then
    mockMvc
        .perform(
            get("/todos/count")
                .param("from", from)
                .param("to", to)
                .header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-todo-count-by-date-range",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Todos")
                        .summary("기간별 ToDo 개수 조회")
                        .description("특정 기간 내 날짜별 목표당 ToDo 개수를 조회합니다.")
                        .queryParameters(
                            parameterWithName("from").description("시작 날짜 (yyyy-MM-dd)"),
                            parameterWithName("to").description("종료 날짜 (yyyy-MM-dd)"))
                        .responseFields(
                            fieldWithPath("data")
                                .type(JsonFieldType.ARRAY)
                                .description("날짜별 ToDo 개수 데이터 배열"),
                            fieldWithPath("data[].date")
                                .type(JsonFieldType.STRING)
                                .description("날짜 (yyyy-MM-dd)"),
                            fieldWithPath("data[].goals")
                                .type(JsonFieldType.ARRAY)
                                .description("목표별 ToDo 개수 배열"),
                            fieldWithPath("data[].goals[].id")
                                .type(JsonFieldType.STRING)
                                .description("목표 ID"),
                            fieldWithPath("data[].goals[].todoCount")
                                .type(JsonFieldType.NUMBER)
                                .description("해당 목표의 ToDo 개수"))
                        .build())));
  }
}
