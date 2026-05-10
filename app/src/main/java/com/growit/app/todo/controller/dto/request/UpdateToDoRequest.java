package com.growit.app.todo.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.growit.app.todo.controller.dto.response.RoutineDto;
import com.growit.app.todo.domain.TodoCategory;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateToDoRequest {
  private String goalId;

  @NotNull(message = "{validation.todo.date.required}")
  private LocalDate date;

  /** 투두 시간 (HH:mm, optional) */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime time;

  @NotBlank(message = "{validation.todo.content.required}")
  @Size(min = 1, max = 30, message = "{validation.todo.content.size}")
  private String content;

  private TodoCategory category; // nullable

  private RoutineDto routine; // nullable
  private RoutineUpdateType routineUpdateType; // nullable
}
