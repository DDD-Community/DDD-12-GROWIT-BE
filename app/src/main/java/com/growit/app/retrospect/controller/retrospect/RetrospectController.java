package com.growit.app.retrospect.controller.retrospect;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.retrospect.controller.retrospect.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.retrospect.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.controller.retrospect.dto.response.RetrospectExistResponse;
import com.growit.app.retrospect.controller.retrospect.dto.response.RetrospectResponse;
import com.growit.app.retrospect.controller.retrospect.mapper.RetrospectRequestMapper;
import com.growit.app.retrospect.controller.retrospect.mapper.RetrospectResponseMapper;
import com.growit.app.retrospect.domain.retrospect.dto.*;
import com.growit.app.retrospect.usecase.retrospect.CheckRetrospectExistsByPlanIdUseCase;
import com.growit.app.retrospect.usecase.retrospect.CreateRetrospectUseCase;
import com.growit.app.retrospect.usecase.retrospect.GetRetrospectByFilterUseCase;
import com.growit.app.retrospect.usecase.retrospect.GetRetrospectUseCase;
import com.growit.app.retrospect.usecase.retrospect.GetRetrospectsByGoalIdUseCase;
import com.growit.app.retrospect.usecase.retrospect.UpdateRetrospectUseCase;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retrospects")
@RequiredArgsConstructor
public class RetrospectController {
  private final CreateRetrospectUseCase createRetrospectUseCase;
  private final UpdateRetrospectUseCase updateRetrospectUseCase;
  private final GetRetrospectUseCase getRetrospectUseCase;
  private final GetRetrospectByFilterUseCase getRetrospectByFilterUseCase;
  private final GetRetrospectsByGoalIdUseCase getRetrospectsByGoalIdUseCase;
  private final CheckRetrospectExistsByPlanIdUseCase checkRetrospectExistsByPlanIdUseCase;

  private final RetrospectRequestMapper retrospectRequestMapper;
  private final RetrospectResponseMapper retrospectResponseMapper;

  @PostMapping
  public ResponseEntity<ApiResponse<IdDto>> createRetrospect(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateRetrospectRequest request) {
    CreateRetrospectCommand command =
        retrospectRequestMapper.toCreateCommand(user.getId(), request);
    String retrospectId = createRetrospectUseCase.execute(command);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(new IdDto(retrospectId)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateRetrospect(
      @AuthenticationPrincipal User user,
      @PathVariable String id,
      @Valid @RequestBody UpdateRetrospectRequest request) {
    UpdateRetrospectCommand command =
        retrospectRequestMapper.toUpdateCommand(id, user.getId(), request);
    updateRetrospectUseCase.execute(command);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<RetrospectResponse>> getRetrospect(
      @AuthenticationPrincipal User user, @PathVariable String id) {
    GetRetrospectQueryFilter filter = retrospectRequestMapper.toGetCommand(id, user.getId());
    RetrospectWithPlan result = getRetrospectUseCase.execute(filter);
    RetrospectResponse response = retrospectResponseMapper.toResponse(result);

    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping(params = {"goalId"})
  public ResponseEntity<ApiResponse<List<RetrospectResponse>>> getRetrospectsByGoalId(
      @AuthenticationPrincipal User user, @RequestParam("goalId") String goalId) {
    List<RetrospectWithPlan> results = getRetrospectsByGoalIdUseCase.execute(goalId, user.getId());
    List<RetrospectResponse> responses =
        results.stream().map(retrospectResponseMapper::toResponse).toList();

    return ResponseEntity.ok(ApiResponse.success(responses));
  }

  @GetMapping(params = {"goalId", "planId"})
  public ResponseEntity<ApiResponse<List<RetrospectResponse>>> getRetrospectByGoalIdAndPlanId(
      @AuthenticationPrincipal User user,
      @RequestParam("goalId") String goalId,
      @RequestParam("planId") String planId) {
    RetrospectQueryFilter filter =
        retrospectRequestMapper.toRetrospectQueryFilter(user.getId(), goalId, planId);
    RetrospectWithPlan result = getRetrospectByFilterUseCase.execute(filter);
    RetrospectResponse response = retrospectResponseMapper.toResponse(result);

    return ResponseEntity.ok(ApiResponse.success(List.of(response)));
  }

  @GetMapping(
      value = "/exists",
      params = {"goalId", "planId"})
  public ResponseEntity<ApiResponse<RetrospectExistResponse>> checkRetrospect(
      @AuthenticationPrincipal User user,
      @RequestParam("goalId") String goalId,
      @RequestParam("planId") String planId) {
    RetrospectQueryFilter filter =
        retrospectRequestMapper.toRetrospectQueryFilter(user.getId(), goalId, planId);
    boolean isExist = checkRetrospectExistsByPlanIdUseCase.execute(filter);
    RetrospectExistResponse response = retrospectResponseMapper.toExistResponse(isExist);

    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
