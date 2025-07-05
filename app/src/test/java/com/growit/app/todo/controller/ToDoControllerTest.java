package com.growit.app.todo.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.BOOLEAN;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
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
import com.growit.app.todo.controller.dto.CompletedStatusChangeRequest;
import com.growit.app.todo.controller.dto.CreateToDoRequest;
import com.growit.app.todo.controller.dto.UpdateToDoRequest;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.usecase.CompletedStatusChangeToDoUseCase;
import com.growit.app.todo.usecase.CreateToDoUseCase;
import com.growit.app.todo.usecase.GetToDoUseCase;
import com.growit.app.todo.usecase.UpdateToDoUseCase;
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
}
