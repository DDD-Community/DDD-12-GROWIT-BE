package com.growit.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueRequest {

  @NotBlank(message = "refreshToken")
  private String refreshToken;
}
