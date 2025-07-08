package com.growit.app.goal.domain.goal.vo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class GoalDurationTest {

  @Test
  void givenNullEndDate_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = LocalDate.now().plusDays(1);
    // When & Then
    assertThrows(BadRequestException.class, () -> new GoalDuration(start, null));
  }

  @Test
  void givenStartDateNotMonday_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = next(DayOfWeek.TUESDAY);
    LocalDate end = start.plusDays(6);
    // When & Then
    assertThrows(BadRequestException.class, () -> new GoalDuration(start, end));
  }

  @Test
  void givenEndDateNotSunday_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = next(DayOfWeek.MONDAY);
    LocalDate end = start.plusDays(5); // Saturday
    // When & Then
    assertThrows(BadRequestException.class, () -> new GoalDuration(start, end));
  }

  @Test
  void givenEndDateBeforeStartDate_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = next(DayOfWeek.MONDAY).plusWeeks(1);
    LocalDate end = next(DayOfWeek.SUNDAY);
    // When & Then
    assertThrows(BadRequestException.class, () -> new GoalDuration(start, end));
  }

  @Test
  void givenStartDateBeforeToday_whenCreatingGoalDuration_thenThrowException() {
    // Given
    LocalDate start = LocalDate.now().minusDays(7);
    while (start.getDayOfWeek() != DayOfWeek.MONDAY) {
      start = start.minusDays(1);
    }
    LocalDate end = start.plusDays(6);
    // When & Then
    LocalDate finalStart = start;
    assertThrows(BadRequestException.class, () -> new GoalDuration(finalStart, end));
  }

  private static LocalDate next(DayOfWeek dayOfWeek) {
    LocalDate date = LocalDate.now().plusDays(1);
    while (date.getDayOfWeek() != dayOfWeek) {
      date = date.plusDays(1);
    }
    return date;
  }
}
