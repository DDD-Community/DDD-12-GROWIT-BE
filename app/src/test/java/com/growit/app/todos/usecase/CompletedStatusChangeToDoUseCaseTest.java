package com.growit.app.todos.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.todos.FakeToDoRepository;
import com.growit.app.fake.todos.ToDoFixture;
import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.dto.CompletedStatusChangeCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompletedStatusChangeToDoUseCaseTest {

  private CompletedStatusChangeToDoUseCase useCase;
  private FakeToDoRepository fakeToDoRepository;

  @BeforeEach
  void setUp() {
    fakeToDoRepository = new FakeToDoRepository();
    useCase = new CompletedStatusChangeToDoUseCase(fakeToDoRepository);

    // ToDoFixture에서 기본 데이터 추가
    ToDo todo = ToDoFixture.defaultToDo();
    fakeToDoRepository.saveToDo(todo);
  }

  @Test
  void givenValidTodo_whenExecute_thenStatusIsChanged() {
    // given
    ToDo todo = fakeToDoRepository.findById("todo-1").get();
    String todoId = todo.getId();
    String userId = todo.getUserId();

    CompletedStatusChangeCommand command = new CompletedStatusChangeCommand(todoId, userId, true);

    // when
    useCase.execute(command);

    // then
    ToDo updated = fakeToDoRepository.findById(todoId).get();
    assertThat(updated.isCompleted()).isTrue();
  }

  @Test
  void givenNonExistingTodo_whenExecute_thenThrowsNotFoundException() {
    // given
    String notExistId = "not-exist";
    String userId = "user-1";

    CompletedStatusChangeCommand command =
        new CompletedStatusChangeCommand(notExistId, userId, true);

    // when & then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("할 일 정보가 존재하지 않습니다.");
  }
}
