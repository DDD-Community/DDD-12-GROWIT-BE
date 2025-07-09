package com.growit.app.todo.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.service.GoalService;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetWeeklyPlanUseCase {
  private final ToDoRepository toDoRepository;
  private final GoalService goalService;
  private final GoalRepository goalRepository;

  @Transactional(readOnly = true)
  public Map<DayOfWeek, List<ToDo>> execute(String goalId, String planId, String userId) {
    Goal goal =
        goalRepository.findById(goalId).orElseThrow(() -> new NotFoundException("목표를 찾을 수 없습니다."));
    goalService.checkPlanExists(userId, goal.getId(), planId);

    List<ToDo> todos = toDoRepository.findByPlanId(planId);

    Map<DayOfWeek, List<ToDo>> map =
        todos.stream()
            .collect(
                Collectors.groupingBy(
                    todo -> todo.getDate().getDayOfWeek(),
                    LinkedHashMap::new,
                    Collectors.toList()));

    for (DayOfWeek day : DayOfWeek.values()) {
      map.putIfAbsent(day, List.of());
    }
    return map;
  }
}
