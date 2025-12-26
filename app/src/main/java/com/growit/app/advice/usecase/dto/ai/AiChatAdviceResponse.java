package com.growit.app.advice.usecase.dto.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiChatAdviceResponse {

  private boolean success;
  private Data data;
  private String timestamp;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Data {
    private String advice;
    private String mode;
  }
}
