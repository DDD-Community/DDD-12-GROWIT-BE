package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetDateQueryFilter;
import com.growit.app.todo.usecase.dto.ToDoWithGoalDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTodosWithGoalByDateUseCase {
  private final ToDoRepository toDoRepository;
  private final GoalRepository goalRepository;

  @Transactional(readOnly = true)
  public List<ToDoWithGoalDto> execute(GetDateQueryFilter filter) {
    List<ToDo> todos = toDoRepository.findByUserIdAndDate(filter.userId(), filter.date());
    
    return todos.stream()
        .map(todo -> {
          Goal goal = null;
          if (todo.getGoalId() != null) {
            Optional<Goal> goalOpt = goalRepository.findById(todo.getGoalId());
            goal = goalOpt.orElse(null);
          }
          return new ToDoWithGoalDto(todo, goal);
        })
        .collect(Collectors.toList());
  }
}