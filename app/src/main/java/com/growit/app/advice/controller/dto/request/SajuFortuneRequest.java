package com.growit.app.advice.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SajuFortuneRequest {

  @NotNull
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식이 올바르지 않습니다. (YYYY-MM-DD)")
  private String birthDate;

  @NotNull
  @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "태어난 시각 형식이 올바르지 않습니다. (HH:mm)")
  private String birthTime;

  @NotNull
  @Pattern(regexp = "^(M|F)$", message = "성별은 M 또는 F여야 합니다.")
  private String gender;
}
