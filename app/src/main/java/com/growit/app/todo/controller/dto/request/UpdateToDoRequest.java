package com.growit.app.todo.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.growit.app.todo.controller.dto.response.RoutineDto;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import com.growit.app.todo.domain.vo.ToDoCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateToDoRequest {
  private String goalId;

  @NotNull(message = "{validation.todo.date.required}")
  private LocalDate date;

  @NotBlank(message = "{validation.todo.content.required}")
  @Size(min = 1, max = 30, message = "{validation.todo.content.size}")
  private String content;

  @JsonProperty("isImportant")
  private Boolean important; // Use Boolean wrapper to allow null values

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ToDoCategory category;

  private RoutineDto routine; // nullable
  private RoutineUpdateType routineUpdateType; // nullable

  public UpdateToDoRequest(
      String goalId,
      LocalDate date,
      String content,
      Boolean important,
      RoutineDto routine,
      RoutineUpdateType routineUpdateType) {
    this(goalId, date, content, important, null, routine, routineUpdateType);
  }
}
