package com.growit.app.fake.goal;

import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.dto.request.GoalDurationDto;
import com.growit.app.goal.controller.dto.response.GoalCreateResponse;
import com.growit.app.goal.controller.dto.response.GoalDetailResponse;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.CreateGoalResult;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.planet.Planet;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.dto.GoalWithAnalysisDto;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class GoalFixture {
  public static Goal defaultGoal() {
    return new GoalBuilder().build();
  }

  public static Goal customGoal(String id, String name, GoalDuration duration) {
    GoalBuilder builder = new GoalBuilder();
    if (id != null) builder.id(id);
    if (name != null) builder.name(name);
    if (duration != null) builder.duration(duration);
    return builder.build();
  }

  public static CreateGoalRequest defaultCreateGoalRequest() {
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    LocalDate endDate =
        startDate.plusWeeks(4).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    return new CreateGoalRequest("내 목표는 그로잇 완성", new GoalDurationDto(startDate, endDate));
  }

  public static UpdateGoalCommand defaultUpdateGoalCommand(String name) {
    Goal goal = defaultGoal();
    return new UpdateGoalCommand(goal.getId(), "user-1", name, goal.getDuration());
  }

  public static GoalWithAnalysisDto defaultGoalWithAnalysis() {
    Goal goal = defaultGoal();
    GoalAnalysis analysis = GoalAnalysis.of(75, "목표가 순조롭게 진행되고 있습니다.");
    return new GoalWithAnalysisDto(goal, analysis);
  }

  public static CreateGoalCommand defaultCreateGoalCommand() {
    return new CreateGoalCommand("user-1", "내 목표는 그로잇 완성", defaultGoal().getDuration());
  }

  public static CreateGoalResult defaultCreateGoalResult() {
    return new CreateGoalResult("goal-1");
  }

  public static GoalCreateResponse defaultGoalCreateResponse() {
    GoalCreateResponse.ImageDto image =
        new GoalCreateResponse.ImageDto("/images/earth_done.png", "/images/earth_progress.png");
    GoalCreateResponse.PlanetDto planet = new GoalCreateResponse.PlanetDto("Earth", image);
    return new GoalCreateResponse("goal-1", planet);
  }

  public static GoalDetailResponse defaultGoalDetailResponse() {
    Goal goal = defaultGoal();
    GoalDetailResponse.ImageDto image =
        new GoalDetailResponse.ImageDto("/images/earth_done.png", "/images/earth_progress.png");
    GoalDetailResponse.PlanetDto planet = new GoalDetailResponse.PlanetDto("Earth", image);
    GoalDetailResponse.DurationDto duration =
        new GoalDetailResponse.DurationDto(
            goal.getDuration().startDate().toString(), goal.getDuration().endDate().toString());
    GoalDetailResponse.AnalysisDto analysis =
        new GoalDetailResponse.AnalysisDto(75, "목표가 순조롭게 진행되고 있습니다.");
    return new GoalDetailResponse(
        goal.getId(),
        goal.getName(),
        planet,
        duration,
        goal.getStatus().toString(),
        analysis,
        false);
  }
}

class GoalBuilder {
  LocalDate today = LocalDate.now();
  LocalDate thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  LocalDate thisSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

  private GoalStatus status = GoalStatus.PROGRESS;
  private GoalDuration duration = new GoalDuration(thisMonday, thisSunday);
  private String id = "goal-1";
  private String userId = "user-1";
  private String name = "테스트 목표";
  private final Planet planet =
      Planet.of(1L, "Earth", "/images/earth_done.png", "/images/earth_progress.png");

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

  public GoalBuilder status(GoalStatus status) {
    this.status = status;
    return this;
  }

  public Goal build() {
    return Goal.create(id, userId, name, planet, duration, status);
  }
}
