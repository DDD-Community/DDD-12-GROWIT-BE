package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetDateRangeQueryFilter;
import com.growit.app.todo.usecase.dto.TodoCountByDateDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTodoCountByGoalInDateRangeUseCase {
  private final ToDoRepository toDoRepository;

  @Transactional(readOnly = true)
  public List<TodoCountByDateDto> execute(GetDateRangeQueryFilter filter) {
    List<ToDo> todos = toDoRepository.findByUserIdAndDateRange(filter);
    
    // Group todos by date
    Map<LocalDate, List<ToDo>> todosByDate = todos.stream()
        .collect(Collectors.groupingBy(ToDo::getDate));

    List<TodoCountByDateDto> result = new ArrayList<>();
    
    // Generate all dates in range, even if no todos exist
    LocalDate currentDate = filter.fromDate();
    while (!currentDate.isAfter(filter.toDate())) {
      List<ToDo> todosForDate = todosByDate.getOrDefault(currentDate, List.of());
      
      // Count todos by goalId for this date
      Map<String, Long> todoCountByGoal = todosForDate.stream()
          .collect(Collectors.groupingBy(
              todo -> todo.getGoalId() != null ? todo.getGoalId() : "기타",
              Collectors.counting()));

      List<TodoCountByDateDto.GoalTodoCount> goalCounts = todoCountByGoal.entrySet()
          .stream()
          .map(entry -> new TodoCountByDateDto.GoalTodoCount(entry.getKey(), entry.getValue().intValue()))
          .collect(Collectors.toList());

      result.add(new TodoCountByDateDto(currentDate, goalCounts));
      currentDate = currentDate.plusDays(1);
    }

    return result;
  }
}