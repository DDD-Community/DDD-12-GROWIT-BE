package com.growit.app.user.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequest {

  @NotBlank(message = "{validation.user.name.required}")
  @Size(min = 2, message = "{validation.user.name.size}")
  private String name;

  private String lastName;

  @Valid private SajuRequest saju;

  @Deprecated
  private String jobRoleId;

  @Deprecated
  private String careerYear;
}
