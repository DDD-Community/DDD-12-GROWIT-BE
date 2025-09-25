package com.growit.app.retrospect.controller.retrospect.mapper;

import com.growit.app.retrospect.controller.retrospect.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.retrospect.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.dto.*;
import com.growit.app.retrospect.domain.retrospect.vo.KPT;
import org.springframework.stereotype.Component;

@Component
public class RetrospectRequestMapper {

  public CreateRetrospectCommand toCreateCommand(String userId, CreateRetrospectRequest request) {
    KPT kpt =
        new KPT(
            request.getKpt().getKeep(),
            request.getKpt().getProblem(),
            request.getKpt().getTryNext());
    return new CreateRetrospectCommand(request.getGoalId(), request.getPlanId(), userId, kpt);
  }

  public UpdateRetrospectCommand toUpdateCommand(
      String id, String userId, UpdateRetrospectRequest request) {
    KPT kpt =
        new KPT(
            request.getKpt().getKeep(),
            request.getKpt().getProblem(),
            request.getKpt().getTryNext());
    return new UpdateRetrospectCommand(id, userId, kpt);
  }

  public GetRetrospectQueryFilter toGetCommand(String id, String userId) {
    return new GetRetrospectQueryFilter(id, userId);
  }

  public RetrospectQueryFilter toRetrospectQueryFilter(
      String userId, String goalId, String planId) {
    return new RetrospectQueryFilter(goalId, planId, userId);
  }
}
