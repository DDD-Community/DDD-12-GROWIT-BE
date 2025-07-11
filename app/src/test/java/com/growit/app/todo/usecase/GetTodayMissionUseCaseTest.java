package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetDateQueryFilter;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetTodayMissionUseCaseTest {

  private ToDoRepository toDoRepository;
  private GetTodayMissionUseCase useCase;

  @BeforeEach
  void setUp() {
    toDoRepository = mock(ToDoRepository.class);
    useCase = new GetTodayMissionUseCase(toDoRepository);
  }

  @Test
  void givenUncompletedTodosExist_whenExecute_thenReturnNotCompletedList() {
    // given
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    ToDo notCompleted = ToDoFixture.customToDo("todo-1", userId, today, "plan-1", "goal-1");
    List<ToDo> toDoList = List.of(notCompleted);
    GetDateQueryFilter queryFilter = new GetDateQueryFilter(userId, today);
    given(toDoRepository.findByUserIdAndDate(userId, today)).willReturn(toDoList);

    // when
    List<ToDo> result = useCase.execute(queryFilter);

    // then
    assertThat(result).containsExactly(notCompleted);
  }

  @Test
  void givenNoTodos_whenExecute_thenReturnNull() {
    // given
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    given(toDoRepository.findByUserIdAndDate(userId, today)).willReturn(List.of());
    GetDateQueryFilter queryFilter = new GetDateQueryFilter(userId, today);
    // when
    List<ToDo> result = useCase.execute(queryFilter);

    // then
    assertThat(result).isNull();
  }

  @Test
  void givenAllTodosCompleted_whenExecute_thenReturnEmptyList() {
    // given
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    ToDo completed = ToDoFixture.customToDo("todo-1", userId, today, "plan-1", "goal-1");
    completed.updateIsCompleted(true); // 직접 완료 처리
    GetDateQueryFilter queryFilter = new GetDateQueryFilter(userId, today);
    given(toDoRepository.findByUserIdAndDate(userId, today)).willReturn(List.of(completed));

    // when
    List<ToDo> result = useCase.execute(queryFilter);

    // then
    assertThat(result).isEmpty();
  }
}
