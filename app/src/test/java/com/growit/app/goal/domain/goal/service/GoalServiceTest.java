package com.growit.app.goal.domain.goal.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class GoalServiceTest {

  private final GoalService goalService = new GoalService();

  @Test
  void givenInvalidPlans_whenCheckPlans_throwBadRequestException() {
    LocalDate start = LocalDate.of(2025, 6, 23);
    LocalDate end = LocalDate.of(2025, 6, 29);
    GoalDuration duration = new GoalDuration(start, end);
    List<PlanDto> plans = List.of(); // 계획이 없음

    assertThrows(BadRequestException.class, () -> goalService.checkPlans(duration, plans));
  }
}
