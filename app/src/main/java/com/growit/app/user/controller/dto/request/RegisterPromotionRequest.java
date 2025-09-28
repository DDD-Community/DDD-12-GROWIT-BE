package com.growit.app.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPromotionRequest {

  @NotBlank(message = "프로모션 코드는 필수입니다.")
  private String code;
}
