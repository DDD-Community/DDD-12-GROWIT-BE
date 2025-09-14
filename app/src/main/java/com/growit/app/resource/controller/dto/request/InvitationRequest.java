package com.growit.app.resource.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record InvitationRequest(
    @NotBlank(message = "validation.invitation.phone.required")
        String phone) {}
