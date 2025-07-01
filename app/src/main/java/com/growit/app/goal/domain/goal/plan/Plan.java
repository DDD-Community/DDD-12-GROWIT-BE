package com.growit.app.goal.domain.goal.plan;

import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Plan {
  private String id;
  private String content;
  private int dayOfMonth;

  public static Plan from(PlanDto dto) {
    return Plan.builder()
        .id(IDGenerator.generateId())
        .dayOfMonth(dto.dayOfMonth())
        .content(dto.content())
        .build();
  }
}
