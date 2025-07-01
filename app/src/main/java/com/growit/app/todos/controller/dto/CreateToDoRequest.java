package com.growit.app.todos.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateToDoRequest {
  @NotBlank(message = "목표명은 필수입니다.")
  @Size(min = 1, max = 30, message = "목표명은 30자 이하여야 합니다.")
  private String goalId;

  @NotBlank(message = "목표명은 필수입니다.")
  @Size(min = 1, max = 30, message = "목표명은 30자 이하여야 합니다.")
  private String planId;

  private LocalDate date;

  @NotBlank(message = "목표명은 필수입니다.")
  @Size(min = 1, max = 30, message = "목표명은 30자 이하여야 합니다.")
  private String content;
}
