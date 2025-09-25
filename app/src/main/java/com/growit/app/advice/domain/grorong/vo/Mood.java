package com.growit.app.advice.domain.grorong.vo;

import java.util.List;
import java.util.Random;

public enum Mood {
  SAD("슬픔", List.of("오늘은 왜 이리 안와…?", "너 기다리다 목 빠질 뻔했어", "너 기다리다 돌이 됐잖아")),
  NORMAL("평범", List.of("꾸준히 나와주는 네 마음이야", "더 열정열정열정!!", "시작이 반이야")),
  HAPPY("행복", List.of("오다 주웠다", "역시 넌 나를 실망시키지 않아"));

  private final String value;
  private final List<String> messages;
  private static final Random random = new Random();

  Mood(String value, List<String> messages) {
    this.value = value;
    this.messages = messages;
  }

  public String getValue() {
    return value;
  }

  public String getMessage() {
    return messages.get(random.nextInt(messages.size()));
  }
}
