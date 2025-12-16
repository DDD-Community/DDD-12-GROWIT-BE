package com.growit.app.todo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.controller.dto.request.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.request.CreateToDoRequest;
import com.growit.app.todo.controller.dto.response.ToDoResponse;
import com.growit.app.todo.controller.mapper.ToDoRequestMapper;
import com.growit.app.todo.controller.mapper.ToDoResponseMapper;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.usecase.*;
import java.time.LocalDate;
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
  @MockitoBean private GetFaceStatusUseCase getFaceStatusUseCase;
  @MockitoBean private GetWeeklyTodoUseCase getWeeklyTodoUseCase;
  @MockitoBean private GetTodayMissionUseCase getTodayMissionUseCase;
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
    CreateToDoRequest request = ToDoFixture.defaultCreateToDoRequest();
    CreateToDoCommand command =
        new CreateToDoCommand("user-1", "goal-1", "할 일 내용", LocalDate.now(), false, null);
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
        .andExpect(jsonPath("$.data").exists());
  }

  @Test
  void getToDoById() throws Exception {
    // given
    String todoId = "todo-123";
    ToDo todo = ToDoFixture.defaultToDo();

    given(toDoRequestMapper.toGetQuery(eq(todoId), any(String.class))).willReturn(null);
    given(getToDoUseCase.execute(any())).willReturn(todo);

    // when & then
    mockMvc
        .perform(get("/todos/{id}", todoId).header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists());
  }

  @Test
  void changeStatus() throws Exception {
    // given
    String todoId = "todo-123";
    CompletedStatusChangeRequest request = new CompletedStatusChangeRequest();

    given(toDoRequestMapper.toCompletedStatusChangeCommand(eq(todoId), any(String.class), any()))
        .willReturn(null);

    // when & then
    mockMvc
        .perform(
            patch("/todos/{id}", todoId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists());
  }
}
