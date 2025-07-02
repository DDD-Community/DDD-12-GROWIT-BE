package com.growit.app.todos.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.growit.app.fake.todos.FakeToDoRepository;
import com.growit.app.fake.todos.ToDoFixture;
import com.growit.app.todos.controller.dto.CreateToDoRequest;
import com.growit.app.todos.domain.CreateToDoCommand;
import com.growit.app.todos.domain.service.ToDoService;
import com.growit.app.todos.domain.service.ToDoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateToDoUseCaseTest {

  private CreateToDoUseCase createToDoUseCase;
  private FakeToDoRepository fakeToDoRepository;

  @BeforeEach
  void setUp() {
    fakeToDoRepository = new FakeToDoRepository();
    ToDoValidator toDoValidator = new ToDoService(fakeToDoRepository);
    createToDoUseCase = new CreateToDoUseCase(toDoValidator, fakeToDoRepository);
  }

  @Test
  void givenValidCommand_whenExecute_thenReturnsToDoId() {
    // Given
    CreateToDoRequest request = ToDoFixture.defaultCreateToDoRequest();

    CreateToDoCommand command =
        new CreateToDoCommand(
            "user-1",
            request.getGoalId(),
            request.getPlanId(),
            request.getContent(),
            request.getDate());

    // When
    String toDoId = createToDoUseCase.execute(command);

    // Then
    assertNotNull(toDoId);
  }
}
