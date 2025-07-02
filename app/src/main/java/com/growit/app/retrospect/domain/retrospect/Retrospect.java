package com.growit.app.retrospect.domain.retrospect;

import com.growit.app.common.util.IDGenerator;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Retrospect {
  private String id;
  private String goalId;
  private String planId;
  private String content;

  public static Retrospect from(CreateRetrospectCommand command) {
    return Retrospect.builder()
        .id(IDGenerator.generateId())
        .goalId(command.goalId())
        .planId(command.planId())
        .content(command.content())
        .build();
  }
}
