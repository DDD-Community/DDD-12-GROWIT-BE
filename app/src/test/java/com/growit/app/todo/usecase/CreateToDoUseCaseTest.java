package com.growit.app.todo.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.growit.app.fake.goal.FakeGoalQuery;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.FakeToDoValidator;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.controller.dto.request.CreateToDoRequest;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.service.ToDoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateToDoUseCaseTest {

  private CreateToDoUseCase createToDoUseCase;

  @BeforeEach
  void setUp() {
    FakeToDoRepository fakeToDoRepository = new FakeToDoRepository();
    FakeGoalRepository fakeGoalRepository = new FakeGoalRepository();
    fakeGoalRepository.saveGoal(GoalFixture.defaultGoal());

    FakeGoalQuery goalQuery = new FakeGoalQuery(fakeGoalRepository);
    ToDoValidator toDoValidator = new FakeToDoValidator();
    createToDoUseCase = new CreateToDoUseCase(goalQuery, toDoValidator, fakeToDoRepository);
  }

  @Test
  void givenValidCommand_whenExecute_thenReturnsToDoId() {
    // Given
    CreateToDoRequest request = ToDoFixture.defaultCreateToDoRequest();

    CreateToDoCommand command =
        new CreateToDoCommand(
            "user-1", request.getGoalId(), request.getContent(), request.getDate());

    // When
    ToDoResult result = createToDoUseCase.execute(command);

    // Then
    assertNotNull(result);
  }
}
