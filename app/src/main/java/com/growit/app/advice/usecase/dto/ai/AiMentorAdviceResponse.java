package com.growit.app.advice.usecase.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiMentorAdviceResponse {

  private boolean success;
  private String userId;
  private String promptId;
  private String id;
  private Output output;
  private String generatedAt;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Output {
    private String keep;
    private String problem;

    @JsonProperty("try")
    private String tryNext;

    private String copywriting;
  }
}
