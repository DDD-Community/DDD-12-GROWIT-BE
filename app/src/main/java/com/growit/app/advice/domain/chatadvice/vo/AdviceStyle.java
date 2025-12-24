package com.growit.app.advice.domain.chatadvice.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdviceStyle {
  BASIC("기본"),
  WARM("대문자F"),
  FACTUAL("팩폭"),
  STRATEGIC("천재전략가");

  private final String label;

  public String getPromptId() {
    return switch (this) {
      case BASIC -> "chat-advice-basic-001";
      case WARM -> "chat-advice-warm-001";
      case FACTUAL -> "chat-advice-factual-001";
      case STRATEGIC -> "chat-advice-strategic-001";
    };
  }
}


