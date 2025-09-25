package com.growit.app.retrospect.controller.retrospect.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KPTDto {

  @NotBlank(message = "{validation.retrospect.content.required}")
  @Size(min = 10, max = 200, message = "{validation.retrospect.content.size}")
  private String keep;

  @NotBlank(message = "{validation.retrospect.content.required}")
  @Size(min = 10, max = 200, message = "{validation.retrospect.content.size}")
  private String problem;

  @NotBlank(message = "{validation.retrospect.content.required}")
  @Size(min = 10, max = 200, message = "{validation.retrospect.content.size}")
  private String tryNext;
}
