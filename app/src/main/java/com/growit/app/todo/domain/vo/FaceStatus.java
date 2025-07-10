package com.growit.app.todo.domain.vo;

public enum FaceStatus {
  SAD,
  NORMAL,
  HAPPY;

  public static FaceStatus from(int completedCnt, int totalCnt) {
    if (totalCnt == 0 || completedCnt == 0) {
      return FaceStatus.SAD;
    } else if (completedCnt == totalCnt) {
      return FaceStatus.HAPPY;
    } else {
      return FaceStatus.NORMAL;
    }
  }
}
