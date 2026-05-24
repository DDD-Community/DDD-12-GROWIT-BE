package com.growit.app.advice.infrastructure.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.growit.app.advice.domain.exception.AiServiceDisabledException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SajuFortuneClientImplTest {

  private final SajuFortuneClientImpl sajuFortuneClientImpl = new SajuFortuneClientImpl();

  @Test
  @DisplayName("getFortune()은 AiServiceDisabledException을 throw한다")
  void getFortuneThrowsAiServiceDisabledException() {
    assertThatThrownBy(() -> sajuFortuneClientImpl.getFortune("1997-01-07", "09:33", "M"))
        .isInstanceOf(AiServiceDisabledException.class);
  }

  @Test
  @DisplayName("getManse()는 빈 Map을 반환한다")
  void getManseReturnsEmptyMap() {
    Map<String, String> result = sajuFortuneClientImpl.getManse("1997-01-07", "09:33", "M");
    assertThat(result).isEmpty();
  }
}
