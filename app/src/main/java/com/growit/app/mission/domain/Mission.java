package com.growit.app.mission.domain;

import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.mission.domain.vo.MissionType;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.todo.domain.ToDo;
import java.time.DayOfWeek;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Mission {
  private String id;
  private DayOfWeek dayOfWeek;
  private String content;
  private MissionType type;
  private boolean finished;

  public void finished(boolean finished) {
    this.finished = finished;
  }

  public static Mission missionStatus(
      MissionType type, DayOfWeek day, List<ToDo> toDoList, Retrospect retrospect, Plan plan) {
    boolean isFinished =
        switch (type) {
          case DAILY_TODO_WRITE ->
              // 투두가 하나라도 작성되어 있으면 완료
              !toDoList.isEmpty();
          case DAILY_TODO_COMPLETE ->
              // 모든 투두가 완료되었으면 완료
              !toDoList.isEmpty() && toDoList.stream().allMatch(ToDo::isCompleted);
          case WEEKLY_GOAL_WRITE ->
              // 주간 목표가 설정되어 있으면 완료
              plan != null && plan.getContent() != null && !plan.getContent().trim().isEmpty();
          case WEEKLY_RETROSPECT_WRITE ->
              // 회고가 작성되어 있으면 완료
              retrospect != null && retrospect.getKpt() != null;
        };

    return Mission.builder()
        .dayOfWeek(day)
        .content(type.getLabel())
        .type(type)
        .finished(isFinished)
        .build();
  }
}
