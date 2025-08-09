package com.growit.app.goal.domain.goalretrospect.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AnalysisTest {

  @Test
  void givenSummaryAndAdvice_whenCreateAnalysis_thenAnalysisCreated() {
    // given
    String summary = "이번 주 목표 달성률이 우수했습니다";
    String advice = "다음 주에는 계획을 좀 더 구체화해보세요";

    // when
    Analysis analysis = new Analysis(summary, advice);

    // then
    assertThat(analysis.summary()).isEqualTo(summary);
    assertThat(analysis.advice()).isEqualTo(advice);
  }

  @Test
  void givenTwoAnalysisWithSameValues_whenCompare_thenEqual() {
    // given
    String summary = "동일한 요약";
    String advice = "동일한 조언";
    Analysis analysis1 = new Analysis(summary, advice);
    Analysis analysis2 = new Analysis(summary, advice);

    // when & then
    assertThat(analysis1).isEqualTo(analysis2);
    assertThat(analysis1.hashCode()).isEqualTo(analysis2.hashCode());
  }
}
