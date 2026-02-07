package com.growit.app.todo.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoutineDto {
  private DurationDto duration;
  private String repeatType;
  private List<DayOfWeek> repeatDays;

  @Getter
  @Builder
  @AllArgsConstructor
  public static class DurationDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
  }
}
