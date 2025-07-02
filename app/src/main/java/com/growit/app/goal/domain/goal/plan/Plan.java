package com.growit.app.goal.domain.goal.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.plan.vo.PlanDuration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class Plan {
  private String id;
  private int weekOfMonth;
  private String content;
  @JsonIgnore
  @Getter(AccessLevel.NONE)
  private PlanDuration planDuration;

  public static Plan from(PlanDto dto, LocalDate goalStart, LocalDate goalEnd) {
    PlanDuration duration = PlanDuration.calculateDuration(dto.weekOfMonth(), goalStart, goalEnd);
    return Plan.builder()
      .id(IDGenerator.generateId())
      .weekOfMonth(dto.weekOfMonth())
      .content(dto.content())
      .planDuration(duration)
      .build();
  }
}
