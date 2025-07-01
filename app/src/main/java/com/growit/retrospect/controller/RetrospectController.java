package com.growit.retrospect.controller;

import com.growit.common.response.ApiResponse;
import com.growit.common.response.IdDto;
import com.growit.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.retrospect.controller.mapper.RetrospectRequestMapper;
import com.growit.retrospect.domain.dto.CreateRetrospectCommand;
import com.growit.retrospect.usecase.CreateRetrospectUseCase;
import com.growit.user.domain.user.User;
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

  @PostMapping
  public ResponseEntity<ApiResponse<IdDto>> createRetrospect(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateRetrospectRequest request) {
    CreateRetrospectCommand command = retrospectRequestMapper.toCommand(user.getId(), request);
    String retrospectId = createRetrospectUseCase.execute(command);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(new IdDto(retrospectId)));
  }
}
