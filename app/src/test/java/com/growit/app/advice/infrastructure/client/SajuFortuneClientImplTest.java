package com.growit.app.advice.infrastructure.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.advice.infrastructure.dto.ForceTellerRequest;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
class SajuFortuneClientImplTest {

  @InjectMocks
  private SajuFortuneClientImpl sajuFortuneClientImpl;

  @Mock
  private WebClient webClient;

  @Test
  @DisplayName("생년월일/시간/성별이 ForceTellerRequest로 올바르게 변환된다")
  void testConversion() throws Exception {
    // Given
    String birthDate = "1997-01-07";
    String birthTime = "09:33";
    String gender = "M";

    // Using reflection to access private method
    Method method =
        SajuFortuneClientImpl.class.getDeclaredMethod(
            "convertToForceTellerRequest", String.class, String.class, String.class);
    method.setAccessible(true);

    // When
    ForceTellerRequest result =
        (ForceTellerRequest) method.invoke(sajuFortuneClientImpl, birthDate, birthTime, gender);

    // Then
    assertThat(result.getBirthday()).isEqualTo("1997/01/07");
    assertThat(result.getBirthtime()).isEqualTo("09:33");
    assertThat(result.getGender()).isEqualTo("M");
    assertThat(result.getYear()).isEqualTo(1997);
    assertThat(result.getMonth()).isEqualTo(1);
    assertThat(result.getDay()).isEqualTo(7);
    assertThat(result.getHour()).isEqualTo(9);
    assertThat(result.getMin()).isEqualTo(33);
    assertThat(result.getCalendar()).isEqualTo("S");
    assertThat(result.isMidnightAdjust()).isTrue();
    assertThat(result.isHmUnsure()).isFalse();
  }
}
