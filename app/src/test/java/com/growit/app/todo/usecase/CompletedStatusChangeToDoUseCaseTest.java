package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.todo.FakeToDoQuery;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.CompletedStatusChangeCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompletedStatusChangeToDoUseCaseTest {

  private CompletedStatusChangeToDoUseCase useCase;
  private FakeToDoRepository fakeToDoRepository;
  private final ToDo todo = ToDoFixture.defaultToDo();

  @BeforeEach
  void setUp() {
    fakeToDoRepository = new FakeToDoRepository();
    useCase =
        new CompletedStatusChangeToDoUseCase(
            fakeToDoRepository, new FakeToDoQuery(fakeToDoRepository));

    fakeToDoRepository.saveToDo(todo);
  }

  @Test
  void givenValidTodo_whenExecute_thenStatusIsChanged() {
    // given
    CompletedStatusChangeCommand command =
        new CompletedStatusChangeCommand(todo.getId(), todo.getUserId(), true, null);

    // when
    useCase.execute(command);

    // then
    ToDo updated = fakeToDoRepository.findById(todo.getId()).get();
    assertThat(updated.isCompleted()).isTrue();
  }

  @Test
  void givenNonExistingTodo_whenExecute_thenThrowsNotFoundException() {
    // given
    String notExistId = "not-exist";
    String userId = "user-1";

    CompletedStatusChangeCommand command =
        new CompletedStatusChangeCommand(notExistId, userId, true, null);

    // when & then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("할 일 정보가 존재하지 않습니다.");
  }
}
