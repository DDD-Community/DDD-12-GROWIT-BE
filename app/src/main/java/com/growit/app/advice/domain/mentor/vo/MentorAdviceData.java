package com.growit.app.advice.domain.mentor.vo;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

/** 멘토 조언 생성에 필요한 데이터를 담는 값 객체 */
@Getter
@Builder
public class MentorAdviceData {

  private final List<String> recentTodos;
  private final List<String> completedTodos;
  private final List<String> incompleteTodos;
  private final List<String> weeklyRetrospects;
  private final List<String> pastWeeklyGoals;
  private final String overallGoal;
}
