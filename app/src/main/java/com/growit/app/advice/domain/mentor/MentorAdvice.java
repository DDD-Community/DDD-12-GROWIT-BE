package com.growit.app.advice.domain.mentor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MentorAdvice {
  private String id;
  private String userId;
  private String goalId;
  private boolean isChecked;
  private String message;
  private Kpt kpt;

  public void updateIsChecked(boolean isChecked) {
    this.isChecked = isChecked;
  }

  @Getter
  @AllArgsConstructor
  public static class Kpt {
    private String keep;
    private String problem;
    private String tryNext;
  }
}
