package com.growit.app.todo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepeatType {
  DAILY("매일"),
  WEEKLY("매주"),
  BIWEEKLY("격주"),
  MONTHLY("매월");

  private final String label;
}
