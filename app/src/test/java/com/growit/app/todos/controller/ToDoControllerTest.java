package com.growit.app.todos.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.todos.FakeToDoRepository;
import com.growit.app.fake.todos.FakeToDoRepositoryConfig;
import com.growit.app.fake.todos.ToDoFixture;
import com.growit.app.todos.controller.dto.CreateToDoRequest;
import com.growit.app.todos.controller.dto.UpdateToDoRequest;
import com.growit.app.todos.domain.ToDoRepository;
import com.growit.app.todos.usecase.CreateToDoUseCase;
import com.growit.app.todos.usecase.UpdateToDoUseCase;
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
public class ToDoControllerTest {
  private MockMvc mockMvc;

  @MockitoBean private CreateToDoUseCase createToDoUseCase;
  @MockitoBean
  private UpdateToDoUseCase updateToDoUseCase;
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

    mockMvc.perform(
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
                fieldWithPath("content").type(STRING).description("수정할 할 일 내용 (5자 이상 30자 미만)"),
                fieldWithPath("date").type(STRING).description("할 일 날짜 (yyyy-MM-dd)")
              )
              .responseFields(
                fieldWithPath("data").type(STRING).description("업데이트 결과 메시지")
              )
              .build()
          )
        )
      );
  }

}
