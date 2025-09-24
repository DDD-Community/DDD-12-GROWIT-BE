package com.growit.app.advice.domain.grorong.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoodTest {

  @Test
  void givenHappyMood_whenGetValue_thenReturnCorrectValue() {
    // given
    Mood mood = Mood.HAPPY;

    // when
    String value = mood.getValue();

    // then
    assertThat(value).isEqualTo("행복");
  }

  @Test
  void givenNormalMood_whenGetValue_thenReturnCorrectValue() {
    // given
    Mood mood = Mood.NORMAL;

    // when
    String value = mood.getValue();

    // then
    assertThat(value).isEqualTo("평범");
  }

  @Test
  void givenSadMood_whenGetValue_thenReturnCorrectValue() {
    // given
    Mood mood = Mood.SAD;

    // when
    String value = mood.getValue();

    // then
    assertThat(value).isEqualTo("슬픔");
  }

  @Test
  void givenHappyMood_whenGetMessage_thenReturnOneOfHappyMessages() {
    // given
    Mood mood = Mood.HAPPY;

    // when
    String message = mood.getMessage();

    // then
    assertThat(message).isIn("오다 주웠다", "역시 넌 나를 실망시키지 않아");
  }

  @Test
  void givenNormalMood_whenGetMessage_thenReturnOneOfNormalMessages() {
    // given
    Mood mood = Mood.NORMAL;

    // when
    String message = mood.getMessage();

    // then
    assertThat(message).isIn("꾸준히 나와주는 네 마음이야", "더 열정열정열정!!", "시작이 반이야");
  }

  @Test
  void givenSadMood_whenGetMessage_thenReturnOneOfSadMessages() {
    // given
    Mood mood = Mood.SAD;

    // when
    String message = mood.getMessage();

    // then
    assertThat(message).isIn("오늘은 왜 이리 안와…?", "너 기다리다 목 빠질 뻔했어", "너 기다리다 돌이 됐잖아");
  }
}
