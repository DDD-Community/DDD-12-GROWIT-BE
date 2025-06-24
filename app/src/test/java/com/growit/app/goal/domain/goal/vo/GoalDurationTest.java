package com.growit.app.goal.domain.goal.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class GoalDurationTest {

  @Test
  void givenDuration_whenCalculateDuration_thenCorrect() {
    LocalDate start = LocalDate.of(2025, 6, 23);
    LocalDate end = LocalDate.of(2025, 7, 20); // 30 days
    GoalDuration duration = new GoalDuration(start, end);

    long expectedWeeks = 4;
    assertEquals(expectedWeeks, duration.getWeekCount());
  }
}
