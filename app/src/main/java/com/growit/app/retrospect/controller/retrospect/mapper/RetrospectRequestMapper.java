package com.growit.app.retrospect.controller.retrospect.mapper;

import com.growit.app.retrospect.controller.retrospect.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.retrospect.dto.request.KPTDto;
import com.growit.app.retrospect.controller.retrospect.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.dto.*;
import com.growit.app.retrospect.domain.retrospect.vo.KPT;
import org.springframework.stereotype.Component;

@Component
public class RetrospectRequestMapper {

  public CreateRetrospectCommand toCreateCommand(String userId, CreateRetrospectRequest request) {
    KPT kpt = getKpt(request.getKpt(), request.getContent());
    return new CreateRetrospectCommand(request.getGoalId(), request.getPlanId(), userId, kpt);
  }

  public UpdateRetrospectCommand toUpdateCommand(
      String id, String userId, UpdateRetrospectRequest request) {
    KPT kpt = getKpt(request.getKpt(), request.getContent());
    return new UpdateRetrospectCommand(id, userId, kpt);
  }

  private static KPT getKpt(KPTDto request, String request1) {
    KPT kpt = null;
    if (request != null) {
      kpt = new KPT(request.getKeep(), request.getProblem(), request.getTryNext());
    } else if (request1 != null) {
      kpt = new KPT("", "", request1);
    }
    return kpt;
  }

  public GetRetrospectQueryFilter toGetCommand(String id, String userId) {
    return new GetRetrospectQueryFilter(id, userId);
  }

  public RetrospectQueryFilter toRetrospectQueryFilter(
      String userId, String goalId, String planId) {
    return new RetrospectQueryFilter(goalId, planId, userId);
  }
}
