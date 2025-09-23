package com.growit.app.advice.domain.grorong.vo;

import java.util.List;
import java.util.Random;

public enum Mood {
  HAPPY(
      "행복",
      List.of("최고의 아이디어는 종종 테이블 위의 커피잔 옆에서 나온다!", "당신의 노력이 빛을 발하고 있어요!", "계속해서 좋은 결과가 나올 것 같아요!")),
  NORMAL("평범", List.of("꾸준히 하다보면 좋은 일이 생길 거예요!", "하루하루 차근차근 해보세요!", "조금씩이라도 발전하고 있어요!")),
  SAD("우울", List.of("요즘은 왜 이리 안와...?", "포기하지 마세요, 응원하고 있어요!", "작은 변화부터 시작해보세요!"));

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
