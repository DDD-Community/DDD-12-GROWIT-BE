package com.growit.app.goal.domain.goal.plan;

import com.growit.app.common.util.IDGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Plan {
  private String id;
  private String content;

  public static Plan from(String content) {
    return Plan.builder().id(IDGenerator.generateId()).content(content).build();
  }
}
