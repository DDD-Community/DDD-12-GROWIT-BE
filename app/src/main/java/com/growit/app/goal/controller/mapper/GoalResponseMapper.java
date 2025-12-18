package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.response.GoalCreateResponse;
import com.growit.app.goal.controller.dto.response.GoalDetailResponse;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import org.springframework.stereotype.Component;

@Component
public class GoalResponseMapper {

  public GoalCreateResponse toCreateResponse(Goal goal) {
    return new GoalCreateResponse(goal.getId(), toPlanetDto(goal));
  }

  public GoalDetailResponse toDetailResponse(Goal goal, GoalAnalysis analysis) {
    return new GoalDetailResponse(
        goal.getId(),
        goal.getName(),
        toDetailPlanetDto(goal),
        toDurationDto(goal),
        mapGoalStatus(goal.getStatus()),
        toAnalysisDto(analysis),
        goal.isCompleted() // isChecked는 목표 완료 여부로 설정
        );
  }

  private GoalCreateResponse.PlanetDto toPlanetDto(Goal goal) {
    var planet = goal.getPlanet();
    return new GoalCreateResponse.PlanetDto(
        planet.name(), new GoalCreateResponse.ImageDto(planet.imageDone(), planet.imageProgress()));
  }

  private GoalDetailResponse.PlanetDto toDetailPlanetDto(Goal goal) {
    var planet = goal.getPlanet();
    return new GoalDetailResponse.PlanetDto(
        planet.name(), new GoalDetailResponse.ImageDto(planet.imageDone(), planet.imageProgress()));
  }

  private GoalDetailResponse.DurationDto toDurationDto(Goal goal) {
    var duration = goal.getDuration();
    return new GoalDetailResponse.DurationDto(
        duration.startDate().toString(), duration.endDate().toString());
  }

  private GoalDetailResponse.AnalysisDto toAnalysisDto(GoalAnalysis analysis) {
    return analysis == null
        ? null
        : new GoalDetailResponse.AnalysisDto(analysis.todoCompletedRate(), analysis.summary());
  }

  private String mapGoalStatus(GoalStatus status) {
    return switch (status) {
      case PROGRESS -> "PROGRESS";
      case COMPLETED, ENDED -> "ENDED";
      default -> "PROGRESS";
    };
  }

  public GoalStatus mapToGoalStatus(String status) {
    if (status == null) {
      return GoalStatus.PROGRESS;
    }
    return switch (status.toUpperCase()) {
      case "PROGRESS" -> GoalStatus.PROGRESS;
      case "ENDED" -> GoalStatus.COMPLETED;
      default -> GoalStatus.PROGRESS;
    };
  }
}
