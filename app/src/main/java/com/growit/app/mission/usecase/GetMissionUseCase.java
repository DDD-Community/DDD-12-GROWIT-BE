package com.growit.app.mission.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.service.MissionSchedule;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetMissionUseCase {
  private final MissionSchedule missionSchedule;
  private final ToDoRepository toDoRepository;
  private final GoalRepository goalRepository;
  private final RetrospectRepository retrospectRepository;

  @Transactional(readOnly = true)
  public List<Mission> execute(String userId) {
    LocalDate today = LocalDate.now();
    DayOfWeek day = today.getDayOfWeek();

    List<Goal> findGoal = goalRepository.findByUserIdAndGoalDuration(userId, today);
    Goal goal = findGoal.stream().findFirst().orElse(null);
    // Plan functionality removed

    List<ToDo> toDoList = toDoRepository.findByUserIdAndDate(userId, today);

    // Get retrospect by other means since plan is no longer available
    // For now, set retrospect to null as plan-based lookup is no longer possible
    Retrospect retrospect = null;

    return missionSchedule.typesFor(day).stream()
        .map(type -> Mission.missionStatus(type, day, toDoList, retrospect))
        .toList();
  }
}
