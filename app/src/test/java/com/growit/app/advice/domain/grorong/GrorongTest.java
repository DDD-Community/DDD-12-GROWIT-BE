package com.growit.app.advice.domain.grorong;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.advice.domain.grorong.vo.Mood;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GrorongTest {

  @Test
  void givenSayingAndMood_whenCreateWithOf_thenReturnGrorong() {
    // given
    String saying = "최고의 아이디어는 종종 테이블 위의 커피잔 옆에서 나온다!";
    Mood mood = Mood.HAPPY;

    // when
    Grorong grorong = Grorong.of(saying, mood);

    // then
    assertThat(grorong.getSaying()).isEqualTo(saying);
    assertThat(grorong.getMood()).isEqualTo(mood);
  }

  @Test
  void givenBuilder_whenBuild_thenReturnGrorong() {
    // given
    String saying = "꾸준히 하다보면 좋은 일이 생길 거예요!";
    Mood mood = Mood.NORMAL;

    // when
    Grorong grorong = Grorong.builder().saying(saying).mood(mood).build();

    // then
    assertThat(grorong.getSaying()).isEqualTo(saying);
    assertThat(grorong.getMood()).isEqualTo(mood);
  }
}
