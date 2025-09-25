package com.growit.app.advice.controller.dto.response;

import com.growit.app.advice.domain.grorong.Grorong;
import lombok.Getter;

@Getter
public class GrorongAdviceResponse {
  private final String saying;
  private final String message;
  private final String mood;

  public GrorongAdviceResponse(Grorong grorong) {
    this.saying = grorong.getSaying();
    this.message = grorong.getMood().getMessage();
    this.mood = grorong.getMood().name();
  }
}
