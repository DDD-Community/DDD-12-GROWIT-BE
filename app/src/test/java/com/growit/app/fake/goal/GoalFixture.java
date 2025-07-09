package com.growit.app.fake.goal;

import com.growit.app.goal.controller.dto.request.BeforeAfterDto;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.dto.request.GoalDurationDto;
import com.growit.app.goal.controller.dto.request.PlanRequestDto;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.plan.vo.PlanDuration;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class GoalFixture {
  public static Goal defaultGoal() {
    return new GoalBuilder().build();
  }

  public static Goal customGoal(
      String id,
      String userId,
      String name,
      GoalDuration duration,
      BeforeAfter beforeAfter,
      List<Plan> plans) {
    GoalBuilder builder = new GoalBuilder();
    if (id != null) builder.id(id);
    if (userId != null) builder.userId(userId);
    if (name != null) builder.name(name);
    if (duration != null) builder.duration(duration);
    if (beforeAfter != null) builder.beforeAfter(beforeAfter);
    if (plans != null) builder.plans(plans);

    return builder.build();
  }

  public static CreateGoalRequest defaultCreateGoalRequest() {
    LocalDate today = LocalDate.now();
    LocalDate startMonday;
    if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
      startMonday = today;
    } else {
      startMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }
    LocalDate endSunday =
        startMonday.plusWeeks(3).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    return new CreateGoalRequest(
        "내 목표는 그로잇 완성",
        new GoalDurationDto(startMonday, endSunday),
        new BeforeAfterDto("기획 정의", "배포 완료"),
        List.of(
            new PlanRequestDto(1, "기획 및 설계 회의"),
            new PlanRequestDto(2, "디자인 시안 뽑기"),
            new PlanRequestDto(3, "프론트 개발 및 백 개발 완료"),
            new PlanRequestDto(4, "배포 완료")));
  }
}

class GoalBuilder {
  LocalDate today = LocalDate.now();
  LocalDate startMonday;

  {
    if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
      startMonday = today;
    } else {
      startMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }
  }

  LocalDate endSunday =
      startMonday.plusWeeks(4).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
  private GoalDuration duration = new GoalDuration(startMonday, endSunday);
  String asIs = "ASIS";
  String toBe = "TOBE";
  private String id = "goal-1";
  private String userId = "user-1";
  private String name = "테스트 목표";
  private BeforeAfter beforeAfter = new BeforeAfter(asIs, toBe);
  private List<Plan> plans =
      List.of(new Plan("plan-1", 1, "그로잇 완성", new PlanDuration(startMonday, endSunday)));
  private boolean isDelete = false;

  public GoalBuilder id(String id) {
    this.id = id;
    return this;
  }

  public GoalBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public GoalBuilder name(String name) {
    this.name = name;
    return this;
  }

  public GoalBuilder duration(GoalDuration duration) {
    this.duration = duration;
    return this;
  }

  public GoalBuilder beforeAfter(BeforeAfter beforeAfter) {
    this.beforeAfter = beforeAfter;
    return this;
  }

  public GoalBuilder plans(List<Plan> plans) {
    this.plans = plans;
    return this;
  }

  public Goal build() {
    return Goal.builder()
        .id(id)
        .userId(userId)
        .name(name)
        .duration(duration)
        .beforeAfter(beforeAfter)
        .plans(plans)
        .isDelete(isDelete)
        .build();
  }
}
