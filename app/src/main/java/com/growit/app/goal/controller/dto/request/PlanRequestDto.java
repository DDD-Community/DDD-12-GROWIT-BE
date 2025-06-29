package com.growit.app.goal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlanRequestDto {
  @NotBlank(message = "주간 계획은 필수입니다.")
  @Size(min = 1, max = 20, message = "주간 계획은 20자 이하여야 합니다.")
  private String content;
}
