package com.growit.app.resource.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.resource.controller.dto.request.InvitationRequest;
import com.growit.app.resource.usecase.CreateInvitationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/externals")
@RequiredArgsConstructor
public class ExternalController {
  private final CreateInvitationUseCase createInvitationUseCase;
  private final MessageService messageService;

  @PostMapping("/invitations")
  public ResponseEntity<ApiResponse<String>> createInvitation(
      @Valid @RequestBody InvitationRequest request) {
    createInvitationUseCase.execute(request.phone());
    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.invitation.sent")));
  }
}
