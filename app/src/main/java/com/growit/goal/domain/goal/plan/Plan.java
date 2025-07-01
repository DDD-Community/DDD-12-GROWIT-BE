package com.growit.goal.domain.goal.plan;

import com.growit.common.util.IDGenerator;
import com.growit.goal.domain.goal.dto.PlanDto;
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

  public static Plan from(PlanDto dto) {
    return Plan.builder()
        .id(IDGenerator.generateId())
        .weekOfMonth(dto.weekOfMonth())
        .content(dto.content())
        .build();
  }
}
