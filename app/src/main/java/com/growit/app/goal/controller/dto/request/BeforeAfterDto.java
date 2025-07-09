package com.growit.app.goal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BeforeAfterDto {
  @NotBlank(message = "{validation.goal.asis.required}")
  @Size(min = 1, max = 20, message = "{validation.goal.asis.size}")
  private String asIs;

  @NotBlank(message = "{validation.goal.tobe.required}")
  @Size(min = 1, max = 20, message = "{validation.goal.tobe.size}")
  private String toBe;
}
