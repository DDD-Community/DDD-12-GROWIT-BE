package com.growit.app.goal.domain.goal.service;

import static com.growit.app.common.util.message.ErrorCode.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalService implements GoalValidator, GoalQuery {
  private final GoalRepository goalRepository;

  @Override
  public void checkGoalDuration(GoalDuration duration) {
    LocalDate startDate = duration.startDate();
    LocalDate endDate = duration.endDate();
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul")).with(DayOfWeek.MONDAY);

    if (startDate.isBefore(today)) {
      throw new BadRequestException(GOAL_DURATION_START_AFTER_TODAY.getCode());
    }

    if (!endDate.isAfter(startDate)) {
      throw new BadRequestException(GOAL_DURATION_START_END.getCode());
    }
  }

  @Override
  public Goal getMyGoal(String id, String userId) throws NotFoundException {
    return goalRepository
        .findByIdAndUserId(id, userId)
        .orElseThrow(() -> new NotFoundException(GOAL_NOT_FOUND.getCode()));
  }

  @Override
  public List<Goal> getFinishedGoalsByYear(String userId, int year) {
    return goalRepository.findAllByUserId(userId).stream()
        .filter(Goal::isCompleted)
        .filter(
            goal -> {
              LocalDate start = goal.getDuration().startDate();
              return start.getYear() == year;
            })
        .sorted(Comparator.comparing((Goal goal) -> goal.getDuration().startDate()).reversed())
        .toList();
  }

  @Override
  public List<Goal> getGoalsByYear(String userId, int year) {
    return goalRepository.findAllByUserId(userId).stream()
        .filter(
            goal -> {
              LocalDate start = goal.getDuration().startDate();
              return start.getYear() == year;
            })
        .sorted(
            Comparator.comparing((Goal goal) -> goal.getDuration().startDate())
                .reversed()
                .thenComparing(
                    (Goal goal) -> goal.getDuration().endDate(), Comparator.reverseOrder()))
        .toList();
  }

  @Override
  public List<Goal> getAllGoalsByUserId(String userId) {
    return goalRepository.findAllByUserId(userId);
  }
}
