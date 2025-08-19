package com.growit.app.mission.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Mission {
  private String id;
  private boolean finished;
  private String content;
  private String userId;
}
