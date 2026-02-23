package com.growit.app.resource.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.resource.controller.dto.request.InvitationRequest;
import com.growit.app.resource.controller.dto.response.InvitationResponse;
import com.growit.app.resource.controller.dto.response.SayingResponse;
import com.growit.app.resource.controller.mapper.ResourceResponseMapper;
import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.usecase.CreateInvitationUseCase;
import com.growit.app.resource.usecase.GetSayingUseCase;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {
  private final GetSayingUseCase getSayingUseCase;
  private final ResourceResponseMapper responseMapper;
  private final CreateInvitationUseCase createInvitationUseCase;
  private final MessageService messageService;

  @GetMapping("/jobroles")
  public ResponseEntity<ApiResponse<Map<String, Object>>> getAllJobRoles() {
    return ResponseEntity.ok(ApiResponse.success(Map.of("jobRoles", Collections.emptyList())));
  }

  @GetMapping("/saying")
  public ResponseEntity<ApiResponse<SayingResponse>> getSaying() {
    Saying saying = getSayingUseCase.execute();

    return ResponseEntity.ok(ApiResponse.success(responseMapper.toSayingResponse(saying)));
  }

  @PostMapping("/invitations")
  public ResponseEntity<ApiResponse<InvitationResponse>> createInvitation(
      @Valid @RequestBody InvitationRequest request) {
    createInvitationUseCase.execute(request.phone());
    String message = messageService.msg("success.invitation.sent");
    return ResponseEntity.ok(ApiResponse.success(InvitationResponse.of(message)));
  }
}
