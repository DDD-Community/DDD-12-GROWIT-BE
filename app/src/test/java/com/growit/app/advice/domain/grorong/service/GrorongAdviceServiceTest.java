package com.growit.app.advice.domain.grorong.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.domain.grorong.vo.Mood;
import com.growit.app.fake.user.UserStatsFixture;
import com.growit.app.user.domain.userstats.UserStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GrorongAdviceServiceTest {

  @InjectMocks private GrorongAdviceService grorongAdviceService;

  @Test
  void givenThreeDayStreakUserStats_whenGenerateAdvice_thenReturnHappyGrorong() {
    // given
    UserStats userStats = UserStatsFixture.threeDayStreakUserStats();
    String saying = "테스트 명언";

    // when
    Grorong result = grorongAdviceService.generateAdvice(userStats, saying);

    // then
    assertThat(result.getSaying()).isEqualTo(saying);
    assertThat(result.getMood()).isEqualTo(Mood.HAPPY);
  }

  @Test
  void givenNotAccessedForThreeDaysUserStats_whenGenerateAdvice_thenReturnSadGrorong() {
    // given
    UserStats userStats = UserStatsFixture.notAccessedForThreeDaysUserStats();
    String saying = "테스트 명언";

    // when
    Grorong result = grorongAdviceService.generateAdvice(userStats, saying);

    // then
    assertThat(result.getSaying()).isEqualTo(saying);
    assertThat(result.getMood()).isEqualTo(Mood.SAD);
  }

  @Test
  void givenNormalUserStats_whenGenerateAdvice_thenReturnNormalGrorong() {
    // given
    UserStats userStats = UserStatsFixture.normalUserStats();
    String saying = "테스트 명언";

    // when
    Grorong result = grorongAdviceService.generateAdvice(userStats, saying);

    // then
    assertThat(result.getSaying()).isEqualTo(saying);
    assertThat(result.getMood()).isEqualTo(Mood.NORMAL);
  }
}
