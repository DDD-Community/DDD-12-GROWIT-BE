package com.growit.app.resource.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InvitationRequest(
    @NotBlank(message = "validation.invitation.phone.required") String phone) {}
