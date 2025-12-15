package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.CreateGoalResult;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.goal.domain.goal.planet.PlanetAssignmentService;
import com.growit.app.goal.domain.goal.planet.Planet;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateGoalUseCase {
  private final GoalValidator goalValidator;
  private final GoalRepository goalRepository;
  private final PlanetAssignmentService planetAssignmentService;

  @CacheEvict(value = "goalCache", key = "#command.userId")
  @Transactional
  public CreateGoalResult execute(CreateGoalCommand command) {
    goalValidator.checkGoalDuration(command.duration());

    // 순환 방식으로 다음 행성 할당
    Planet assignedPlanet = planetAssignmentService.getNextPlanet(command.userId());
    Goal goal = Goal.from(command, assignedPlanet);
    goalRepository.saveGoal(goal);

    return new CreateGoalResult(goal.getId());
  }
}
