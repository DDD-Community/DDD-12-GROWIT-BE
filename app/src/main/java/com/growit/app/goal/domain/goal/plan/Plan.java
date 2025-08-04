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

  @JsonIgnore private PlanDuration planDuration;

  public static Plan from(PlanDto dto, LocalDate goalStart) {
    PlanDuration duration = PlanDuration.calculateDuration(dto.weekOfMonth(), goalStart);
    return Plan.builder()
        .id(IDGenerator.generateId())
        .weekOfMonth(dto.weekOfMonth())
        .content(dto.content())
        .planDuration(duration)
        .build();
  }

  public void updateByPlan(PlanDto dto, PlanDuration duration) {
    this.weekOfMonth = dto.weekOfMonth();
    this.content = dto.content();
    this.planDuration = duration;
  }
}
