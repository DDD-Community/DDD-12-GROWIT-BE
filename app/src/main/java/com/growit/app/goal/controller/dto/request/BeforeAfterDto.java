package com.growit.app.goal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BeforeAfterDto {
  @NotBlank(message = "현재 상태는 필수입니다.")
  @Size(min = 1, max = 20, message = "현재 상태는 20자 이하여야 합니다.")
  private String asIs;

  @NotBlank(message = "목표 상태는 필수입니다.")
  @Size(min = 1, max = 20, message = "목표 상태는 20자 이하여야 합니다.")
  private String toBe;
}
