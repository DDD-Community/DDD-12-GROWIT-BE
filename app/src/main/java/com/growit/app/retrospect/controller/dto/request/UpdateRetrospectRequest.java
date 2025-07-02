package com.growit.app.retrospect.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRetrospectRequest {
  @NotBlank(message = "회고 내용은 필수입니다.")
  @Size(min = 10, max = 200, message = "회고 내용은 10자 이상 200자 이하여야 합니다.")
  private String content;
}
