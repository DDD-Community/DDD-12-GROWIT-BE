package com.growit.app.todo.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateToDoRequest {
  @NotBlank(message = "{validation.todo.goal-id.required}")
  @Size(min = 1)
  private String goalId;

  @NotBlank(message = "{validation.todo.plan-id.required}")
  @Size(min = 1)
  private String planId;

  @NotNull(message = "{validation.todo.date.required}")
  private LocalDate date;

  @NotBlank(message = "{validation.todo.content.required}")
  @Size(min = 5, max = 30, message = "{validation.todo.content.size}")
  private String content;
}
