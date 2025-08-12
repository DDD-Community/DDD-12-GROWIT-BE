package com.growit.app.goal.domain.goal.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalCategory {
  UNCATEGORIZED("정의되지 않은 카테고리"),
  PROFESSIONAL_GROWTH("전문성 & 기술 성장"),
  CAREER_TRANSITION("취업 & 커리어 전환"),
  LIFESTYLE_ROUTINE("루틴 & 생활 습관"),
  WEALTH_BUILDING("제테크 & 자산증식"),
  SIDE_PROJECT("사이드 프로젝트"),
  NETWORKING("네트워킹 & 인맥 구축");

  private final String label;
}
