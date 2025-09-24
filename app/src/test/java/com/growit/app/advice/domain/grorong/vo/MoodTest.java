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
    assertThat(value).isEqualTo("우울");
  }

  @Test
  void givenHappyMood_whenGetMessage_thenReturnOneOfHappyMessages() {
    // given
    Mood mood = Mood.HAPPY;

    // when
    String message = mood.getMessage();

    // then
    assertThat(message)
        .isIn("최고의 아이디어는 종종 테이블 위의 커피잔 옆에서 나온다!", "당신의 노력이 빛을 발하고 있어요!", "계속해서 좋은 결과가 나올 것 같아요!");
  }

  @Test
  void givenNormalMood_whenGetMessage_thenReturnOneOfNormalMessages() {
    // given
    Mood mood = Mood.NORMAL;

    // when
    String message = mood.getMessage();

    // then
    assertThat(message).isIn("꾸준히 하다보면 좋은 일이 생길 거예요!", "하루하루 차근차근 해보세요!", "조금씩이라도 발전하고 있어요!");
  }

  @Test
  void givenSadMood_whenGetMessage_thenReturnOneOfSadMessages() {
    // given
    Mood mood = Mood.SAD;

    // when
    String message = mood.getMessage();

    // then
    assertThat(message).isIn("요즘은 왜 이리 안와...?", "포기하지 마세요, 응원하고 있어요!", "작은 변화부터 시작해보세요!");
  }
}
