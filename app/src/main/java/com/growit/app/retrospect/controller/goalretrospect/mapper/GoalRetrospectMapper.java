package com.growit.app.retrospect.controller.goalretrospect.mapper;

import com.growit.app.retrospect.controller.goalretrospect.dto.request.CreateGoalRetrospectRequest;
import com.growit.app.retrospect.controller.goalretrospect.dto.request.UpdateGoalRetrospectRequest;
import com.growit.app.retrospect.controller.goalretrospect.dto.response.GoalRetrospectResponse;
import com.growit.app.retrospect.controller.goalretrospect.dto.response.GoalWithGoalRetrospectResponse;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.dto.CreateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.dto.UpdateGoalRetrospectCommand;
import com.growit.app.retrospect.usecase.goalretrospect.dto.GoalWithGoalRetrospectDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GoalRetrospectMapper {

  public CreateGoalRetrospectCommand toCreateCommand(
      String userId, CreateGoalRetrospectRequest request) {
    return new CreateGoalRetrospectCommand(userId, request.getGoalId());
  }

  public UpdateGoalRetrospectCommand toUpdateCommand(
      String id, String userId, UpdateGoalRetrospectRequest request) {
    return new UpdateGoalRetrospectCommand(id, userId, request.getContent());
  }

  public GoalRetrospectResponse toResponse(GoalRetrospect goalRetrospect) {
    return new GoalRetrospectResponse(
        goalRetrospect.getId(),
        goalRetrospect.getGoalId(),
        goalRetrospect.getTodoCompletedRate(),
        goalRetrospect.getAnalysis(),
        goalRetrospect.getContent());
  }

  public List<GoalWithGoalRetrospectResponse> toResponse(List<GoalWithGoalRetrospectDto> results) {
    if (results == null || results.isEmpty()) {
      return List.of();
    }
    return results.stream().map(GoalRetrospectMapper::from).toList();
  }

  public static GoalWithGoalRetrospectResponse from(GoalWithGoalRetrospectDto dto) {
    return new GoalWithGoalRetrospectResponse(
        new GoalWithGoalRetrospectResponse.GoalDto(
            dto.goal().getId(),
            dto.goal().getName(),
            new GoalWithGoalRetrospectResponse.GoalDto.DurationDto(
                dto.goal().getDuration().startDate(), dto.goal().getDuration().endDate())),
        dto.goalRetrospect() == null
            ? null
            : new GoalWithGoalRetrospectResponse.GoalRetrospectDto(
                dto.goalRetrospect().getId(), !dto.goalRetrospect().getContent().isEmpty()));
  }
}
