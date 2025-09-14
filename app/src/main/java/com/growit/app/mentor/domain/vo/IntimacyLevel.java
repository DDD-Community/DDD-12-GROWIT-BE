package com.growit.app.mentor.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IntimacyLevel {
  HIGH("상"),
  MEDIUM("중"),
  LOW("하");

  private final String label;
}
