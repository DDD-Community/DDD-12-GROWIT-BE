package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.response.GoalCreateResponse;
import com.growit.app.goal.controller.dto.response.GoalDetailResponse;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GoalResponseMapper {

  public GoalCreateResponse toCreateResponse(Goal goal) {
    return new GoalCreateResponse(
        goal.getId(),
        toPlanetDto(goal)
    );
  }

  public GoalDetailResponse toDetailResponse(Goal goal) {
    return new GoalDetailResponse(
        goal.getId(),
        goal.getName(),
        toDetailPlanetDto(goal),
        toDurationDto(goal),
        mapGoalStatus(goal.getStatus()),
        createDefaultAnalysis(), // 기본 분석 정보 제공
        LocalDateTime.now() // TODO: Goal에 createdAt 필드 추가 필요
    );
  }

  private GoalCreateResponse.PlanetDto toPlanetDto(Goal goal) {
    var planet = goal.getPlanet();
    return new GoalCreateResponse.PlanetDto(
        planet.name(),
        new GoalCreateResponse.ImageDto(planet.imageDone(), planet.imageProgress())
    );
  }

  private GoalDetailResponse.PlanetDto toDetailPlanetDto(Goal goal) {
    var planet = goal.getPlanet();
    return new GoalDetailResponse.PlanetDto(
        planet.name(),
        new GoalDetailResponse.ImageDto(planet.imageDone(), planet.imageProgress())
    );
  }

  private GoalDetailResponse.DurationDto toDurationDto(Goal goal) {
    var duration = goal.getDuration();
    return new GoalDetailResponse.DurationDto(
        duration.startDate().toString(),
        duration.endDate().toString()
    );
  }

  private GoalDetailResponse.AnalysisDto createDefaultAnalysis() {
    // 기본 분석 정보를 제공
    return new GoalDetailResponse.AnalysisDto(
        0,
        "목표를 시작했습니다. 화이팅!"
    );
  }

  private String mapGoalStatus(com.growit.app.goal.domain.goal.vo.GoalStatus status) {
    return switch (status) {
      case IN_PROGRESS, PROGRESS -> "PROGRESS";
      case COMPLETED, ENDED -> "ENDED";
      default -> "PROGRESS";
    };
  }
}