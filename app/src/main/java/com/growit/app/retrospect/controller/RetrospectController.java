package com.growit.app.retrospect.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.controller.dto.response.RetrospectResponse;
import com.growit.app.retrospect.controller.mapper.RetrospectRequestMapper;
import com.growit.app.retrospect.controller.mapper.RetrospectResponseMapper;
import com.growit.app.retrospect.domain.retrospect.command.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.command.GetRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.command.UpdateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectWithPlan;
import com.growit.app.retrospect.usecase.CreateRetrospectUseCase;
import com.growit.app.retrospect.usecase.GetRetrospectUseCase;
import com.growit.app.retrospect.usecase.UpdateRetrospectUseCase;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
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
  private final RetrospectRequestMapper retrospectRequestMapper;
  private final UpdateRetrospectUseCase updateRetrospectUseCase;
  private final GetRetrospectUseCase getRetrospectUseCase;

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

    GetRetrospectCommand command = retrospectRequestMapper.toGetCommand(id, user.getId());

    RetrospectWithPlan result = getRetrospectUseCase.execute(command);

    RetrospectResponse response =
        RetrospectResponseMapper.toResponse(result.getRetrospect(), result.getPlan());
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
