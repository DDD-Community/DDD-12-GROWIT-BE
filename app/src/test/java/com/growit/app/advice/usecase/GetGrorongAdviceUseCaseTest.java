package com.growit.app.advice.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.domain.grorong.service.GrorongAdviceService;
import com.growit.app.fake.advice.MentorAdviceFixture;
import com.growit.app.fake.resource.SayingFixture;
import com.growit.app.fake.user.UserStatsFixture;
import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.usecase.GetSayingUseCase;
import com.growit.app.user.domain.userstats.UserStats;
import com.growit.app.user.domain.userstats.UserStatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetGrorongAdviceUseCaseTest {

  @Mock private GetSayingUseCase getSayingUseCase;
  @Mock private UserStatsRepository userStatsRepository;
  @Mock private GrorongAdviceService grorongAdviceService;

  @InjectMocks private GetGrorongAdviceUseCase getGrorongAdviceUseCase;

  @Test
  void givenUserId_whenExecute_thenReturnGrorong() {
    // given
    String userId = "user-1";
    UserStats userStats = UserStatsFixture.threeDayStreakUserStats();
    Saying saying = SayingFixture.defaultSaying();
    Grorong expectedGrorong = MentorAdviceFixture.happyGrorong();

    given(userStatsRepository.findByUserId(userId)).willReturn(userStats);
    given(getSayingUseCase.execute()).willReturn(saying);
    given(grorongAdviceService.generateAdvice(userStats, saying.getMessage()))
        .willReturn(expectedGrorong);

    // when
    Grorong result = getGrorongAdviceUseCase.execute(userId);

    // then
    assertThat(result).isEqualTo(expectedGrorong);
  }

  @Test
  void givenUserIdWithNormalStats_whenExecute_thenReturnNormalGrorong() {
    // given
    String userId = "user-1";
    UserStats userStats = UserStatsFixture.normalUserStats();
    Saying saying = SayingFixture.defaultSaying();
    Grorong expectedGrorong = MentorAdviceFixture.normalGrorong();

    given(userStatsRepository.findByUserId(userId)).willReturn(userStats);
    given(getSayingUseCase.execute()).willReturn(saying);
    given(grorongAdviceService.generateAdvice(userStats, saying.getMessage()))
        .willReturn(expectedGrorong);

    // when
    Grorong result = getGrorongAdviceUseCase.execute(userId);

    // then
    assertThat(result).isEqualTo(expectedGrorong);
  }

  @Test
  void givenUserIdWithSadStats_whenExecute_thenReturnSadGrorong() {
    // given
    String userId = "user-1";
    UserStats userStats = UserStatsFixture.notAccessedForThreeDaysUserStats();
    Saying saying = SayingFixture.defaultSaying();
    Grorong expectedGrorong = MentorAdviceFixture.sadGrorong();

    given(userStatsRepository.findByUserId(userId)).willReturn(userStats);
    given(getSayingUseCase.execute()).willReturn(saying);
    given(grorongAdviceService.generateAdvice(userStats, saying.getMessage()))
        .willReturn(expectedGrorong);

    // when
    Grorong result = getGrorongAdviceUseCase.execute(userId);

    // then
    assertThat(result).isEqualTo(expectedGrorong);
  }
}
