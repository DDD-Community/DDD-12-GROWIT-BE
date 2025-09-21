package com.growit.app.batch.domain.batchjob.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchJobStatus {
  PENDING("대기중"),
  RUNNING("실행중"),
  COMPLETED("완료"),
  FAILED("실패");

  private final String description;
}
