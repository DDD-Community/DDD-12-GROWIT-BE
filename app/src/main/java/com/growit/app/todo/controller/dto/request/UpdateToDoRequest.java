package com.growit.app.todo.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
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
}
