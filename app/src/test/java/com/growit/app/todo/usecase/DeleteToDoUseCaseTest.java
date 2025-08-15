package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.goal.FakeGoalStatusUpdater;
import com.growit.app.fake.todo.FakeToDoQuery;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.DeleteToDoCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteToDoUseCaseTest {

  private DeleteToDoUseCase useCase;
  private FakeToDoRepository fakeToDoRepository;
  private final ToDo todo = ToDoFixture.defaultToDo();

  @BeforeEach
  void setUp() {
    fakeToDoRepository = new FakeToDoRepository();
    FakeGoalStatusUpdater goalStatusUpdater = new FakeGoalStatusUpdater();
    useCase =
        new DeleteToDoUseCase(
            fakeToDoRepository, goalStatusUpdater, new FakeToDoQuery(fakeToDoRepository));

    fakeToDoRepository.saveToDo(todo);
  }

  @Test
  void givenValidTodo_whenExecute_thenSuccess() {
    // given
    DeleteToDoCommand command = new DeleteToDoCommand(todo.getId(), todo.getUserId());

    // when
    useCase.execute(command);

    // then
    boolean isDeleted = fakeToDoRepository.findById(todo.getId()).isEmpty();
    assertThat(isDeleted).isTrue();
  }
}
