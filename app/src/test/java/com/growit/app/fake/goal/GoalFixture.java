package com.growit.app.fake.goal;

import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.dto.request.GoalDurationDto;
import com.growit.app.goal.controller.dto.request.PlanRequestDto;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.plan.vo.PlanDuration;
import com.growit.app.goal.domain.goal.vo.GoalCategory;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.goal.domain.goal.vo.GoalUpdateStatus;
import com.growit.app.goal.domain.goal.vo.Mentor;
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
      String toBe,
      GoalCategory category,
      GoalUpdateStatus updateStatus,
      List<Plan> plans,
      Mentor mentor) {
    GoalBuilder builder = new GoalBuilder();
    if (id != null) builder.id(id);
    if (userId != null) builder.userId(userId);
    if (name != null) builder.name(name);
    if (duration != null) builder.duration(duration);
    if (toBe != null) builder.toBe(toBe);
    if (category != null) builder.category(category);
    if (updateStatus != null) builder.updateStatus(updateStatus);
    if (plans != null) builder.plans(plans);
    if (mentor != null) builder.mentor(mentor);

    return builder.build();
  }

  public static CreateGoalRequest defaultCreateGoalRequest() {
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    LocalDate endDate =
        startDate.plusWeeks(4).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    return new CreateGoalRequest(
        "내 목표는 그로잇 완성",
        new GoalDurationDto(startDate, endDate),
        "배포 완료",
        GoalCategory.STUDY,
        List.of(
            new PlanRequestDto(1, "기획 및 설계 회의"),
            new PlanRequestDto(2, "디자인 시안 뽑기"),
            new PlanRequestDto(3, "프론트 개발 및 백 개발 완료"),
            new PlanRequestDto(4, "배포 완료")));
  }

  public static UpdateGoalCommand defaultUpdateGoalCommand(String name, List<PlanDto> plans) {
    Goal goal = defaultGoal();
    return new UpdateGoalCommand(
        goal.getId(),
        goal.getUserId(),
        name,
        goal.getDuration(),
        goal.getToBe(),
        goal.getCategory(),
        plans);
  }
}

class GoalBuilder {
  LocalDate today = LocalDate.now();
  LocalDate thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  private List<Plan> plans =
      List.of(new Plan("plan-1", 1, "그로잇 완성", PlanDuration.calculateDuration(1, thisMonday)));
  LocalDate thisSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
  private GoalDuration duration = new GoalDuration(thisMonday, thisSunday);
  private String id = "goal-1";
  private String userId = "user-1";
  private String name = "테스트 목표";
  private String toBe = "TOBE";
  private GoalCategory category = GoalCategory.STUDY;
  private GoalUpdateStatus updateStatus = GoalUpdateStatus.UPDATABLE;
  private Mentor mentor = Mentor.TIM_COOK;
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

  public GoalBuilder toBe(String toBe) {
    this.toBe = toBe;
    return this;
  }

  public GoalBuilder category(GoalCategory category) {
    this.category = category;
    return this;
  }

  public GoalBuilder updateStatus(GoalUpdateStatus updateStatus) {
    this.updateStatus = updateStatus;
    return this;
  }

  public GoalBuilder plans(List<Plan> plans) {
    this.plans = plans;
    return this;
  }

  public GoalBuilder mentor(Mentor mentor) {
    this.mentor = mentor;
    return this;
  }

  public Goal build() {
    return Goal.builder()
        .id(id)
        .userId(userId)
        .name(name)
        .duration(duration)
        .toBe(toBe)
        .category(category)
        .plans(plans)
        .mentor(mentor)
        .isDelete(isDelete)
        .build();
  }
}
