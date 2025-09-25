package com.growit.app.advice.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.advice.controller.dto.response.MentorAdviceResponse;
import com.growit.app.advice.domain.mentor.MentorAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MentorAdviceResponseMapper 테스트")
class MentorAdviceResponseMapperTest {

  private MentorAdviceResponseMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new MentorAdviceResponseMapper();
  }

  @Test
  @DisplayName("MentorAdvice를 MentorAdviceResponse로 변환")
  void toResponse() {
    // given
    MentorAdvice.Kpt kpt =
        new MentorAdvice.Kpt(
            "투두 분해와 실행 리듬은 안정적이다.", "디자인 - 개발 QA 협업 속도가 더디다", "이번주는 MVP를 반드시 배포해라.");
    MentorAdvice mentorAdvice =
        new MentorAdvice("1", "user123", "goal456", true, "혁신은 단순함에서 시작돼", kpt);

    // when
    MentorAdviceResponse response = mapper.toResponse(mentorAdvice);

    // then
    assertThat(response.isChecked()).isTrue();
    assertThat(response.message()).isEqualTo("혁신은 단순함에서 시작돼");
    assertThat(response.kpt().keep()).isEqualTo("투두 분해와 실행 리듬은 안정적이다.");
    assertThat(response.kpt().problem()).isEqualTo("디자인 - 개발 QA 협업 속도가 더디다");
    assertThat(response.kpt().tryNext()).isEqualTo("이번주는 MVP를 반드시 배포해라.");
  }

  @Test
  @DisplayName("isChecked가 false인 경우 변환 테스트")
  void toResponseWhenNotChecked() {
    // given
    MentorAdvice.Kpt kpt = new MentorAdvice.Kpt("keep", "problem", "tryNext");
    MentorAdvice mentorAdvice = new MentorAdvice("1", "user123", "goal456", false, "테스트 메시지", kpt);

    // when
    MentorAdviceResponse response = mapper.toResponse(mentorAdvice);

    // then
    assertThat(response.isChecked()).isFalse();
    assertThat(response.message()).isEqualTo("테스트 메시지");
    assertThat(response.kpt().keep()).isEqualTo("keep");
    assertThat(response.kpt().problem()).isEqualTo("problem");
    assertThat(response.kpt().tryNext()).isEqualTo("tryNext");
  }
}
