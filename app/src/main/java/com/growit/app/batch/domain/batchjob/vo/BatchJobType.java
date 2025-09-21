package com.growit.app.batch.domain.batchjob.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchJobType {
  DAILY_ADVICE_GENERATION("일일 조언 생성", "0 0 0 * * *"),
  WEEKLY_PLAN_RECOMMENDATION("주간 목표 추천", "0 0 0 * * MON"),
  USER_DATA_SYNC("사용자 데이터 동기화", "0 0 2 * * *");

  private final String description;
  private final String cronExpression;
}
