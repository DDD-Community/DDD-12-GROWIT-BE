package com.growit.app.retrospect.domain.retrospect;

import com.growit.app.common.util.IDGenerator;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.dto.UpdateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.vo.KPT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Retrospect {
  private String id;
  private String userId;
  private String goalId;
  private String planId;
  private KPT kpt;

  public static Retrospect from(CreateRetrospectCommand command) {
    return Retrospect.builder()
        .id(IDGenerator.generateId())
        .userId(command.userId())
        .goalId(command.goalId())
        .planId(command.planId())
        .kpt(command.kpt())
        .build();
  }

  public void updateBy(UpdateRetrospectCommand command) {
    this.kpt = command.kpt();
  }
}
