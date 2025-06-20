package com.growit.app.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueRequest {

  @NotBlank(message = "refreshToken")
  private String refreshToken;
}
