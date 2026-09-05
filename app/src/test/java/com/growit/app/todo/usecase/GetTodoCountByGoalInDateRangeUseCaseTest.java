package com.growit.app.todo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetDateRangeQueryFilter;
import com.growit.app.todo.domain.vo.ToDoCategory;
import com.growit.app.todo.usecase.dto.TodoCountByDateDto;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTodoCountByGoalInDateRangeUseCaseTest {

  @Mock private ToDoRepository toDoRepository;
  @InjectMocks private GetTodoCountByGoalInDateRangeUseCase useCase;

  @Test
  void countsTodosAndCompletedTodosByCategoryForEachDate() {
    LocalDate date = LocalDate.of(2026, 9, 5);
    GetDateRangeQueryFilter filter = new GetDateRangeQueryFilter("user-1", date, date);
    given(toDoRepository.findByUserIdAndDateRange(filter))
        .willReturn(
            List.of(
                todo("now-1", date, ToDoCategory.NOW, true),
                todo("now-2", date, ToDoCategory.NOW, false),
                todo("delete-1", date, ToDoCategory.DELETE, false)));

    List<TodoCountByDateDto> result = useCase.execute(filter);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getCategoryCounts())
        .extracting(
            TodoCountByDateDto.CategoryTodoCount::getCategory,
            TodoCountByDateDto.CategoryTodoCount::getTodoCount,
            TodoCountByDateDto.CategoryTodoCount::getCompletedCount)
        .containsExactlyInAnyOrder(
            org.assertj.core.groups.Tuple.tuple(ToDoCategory.NOW, 2, 1),
            org.assertj.core.groups.Tuple.tuple(ToDoCategory.DELETE, 1, 0));
  }

  private ToDo todo(String id, LocalDate date, ToDoCategory category, boolean completed) {
    return ToDo.builder()
        .id(id)
        .userId("user-1")
        .goalId("goal-1")
        .date(date)
        .content(id)
        .category(category)
        .isCompleted(completed)
        .build();
  }
}
