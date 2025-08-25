package com.growit.app.goal.domain.goal.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.plan.vo.PlanDuration;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Plan {
  private String id;
  private int weekOfMonth;
  private String content;

  private PlanDuration duration;

  @JsonIgnore
  public boolean isCurrentWeek() {
    return duration.includes(LocalDate.now());
  }

  public static Plan from(PlanDto dto, LocalDate goalStart) {
    PlanDuration duration = PlanDuration.calculateDuration(dto.weekOfMonth(), goalStart);
    return Plan.builder()
        .id(IDGenerator.generateId())
        .weekOfMonth(dto.weekOfMonth())
        .content(dto.content())
        .duration(duration)
        .build();
  }

  public void updateByPlan(String content) {
    this.content = content;
  }
}
