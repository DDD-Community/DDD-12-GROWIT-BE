package com.growit.app.todo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateToDoRequest {

  @NotNull(message = "날짜는 필수 입니다.")
  private LocalDate date;

  @NotBlank(message = "할일 내용은 필수입니다.")
  @Size(min = 5, max = 30, message = "할 일은 5~30자 사이여야 합니다.")
  private String content;
}
