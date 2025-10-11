package com.growit.app.advice.domain.mentor;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MentorAdvice 도메인 테스트")
class MentorAdviceTest {

  @Test
  @DisplayName("MentorAdvice 객체 생성 테스트")
  void createMentorAdvice() {
    // given
    String id = "1";
    String userId = "user123";
    String goalId = "goal456";
    boolean isChecked = false;
    String message = "혁신은 단순함에서 시작돼";
    MentorAdvice.Kpt kpt =
        new MentorAdvice.Kpt(
            "투두 분해와 실행 리듬은 안정적이다.", "디자인 - 개발 QA 협업 속도가 더디다", "이번주는 MVP를 반드시 배포해라.");

    // when
    MentorAdvice mentorAdvice =
        new MentorAdvice(
            id, userId, goalId, isChecked, message, kpt, LocalDateTime.now(ZoneOffset.UTC));

    // then
    assertThat(mentorAdvice.getId()).isEqualTo(id);
    assertThat(mentorAdvice.getUserId()).isEqualTo(userId);
    assertThat(mentorAdvice.getGoalId()).isEqualTo(goalId);
    assertThat(mentorAdvice.isChecked()).isEqualTo(isChecked);
    assertThat(mentorAdvice.getMessage()).isEqualTo(message);
    assertThat(mentorAdvice.getKpt()).isEqualTo(kpt);
  }

  @Test
  @DisplayName("isChecked 값 업데이트 테스트")
  void updateIsChecked() {
    // given
    MentorAdvice.Kpt kpt = new MentorAdvice.Kpt("keep", "problem", "tryNext");
    MentorAdvice mentorAdvice =
        new MentorAdvice(
            "1", "user123", "goal456", false, "message", kpt, LocalDateTime.now(ZoneOffset.UTC));

    // when
    mentorAdvice.updateIsChecked(true);

    // then
    assertThat(mentorAdvice.isChecked()).isTrue();
  }

  @Test
  @DisplayName("KPT 객체 생성 테스트")
  void createKpt() {
    // given
    String keep = "투두 분해와 실행 리듬은 안정적이다.";
    String problem = "디자인 - 개발 QA 협업 속도가 더디다";
    String tryNext = "이번주는 MVP를 반드시 배포해라.";

    // when
    MentorAdvice.Kpt kpt = new MentorAdvice.Kpt(keep, problem, tryNext);

    // then
    assertThat(kpt.getKeep()).isEqualTo(keep);
    assertThat(kpt.getProblem()).isEqualTo(problem);
    assertThat(kpt.getTryNext()).isEqualTo(tryNext);
  }

  @Test
  @DisplayName("shouldFetch - 24시간 이상 지난 경우 true 반환")
  void shouldFetch_whenMoreThan24HoursOld_returnTrue() {
    // given
    LocalDateTime oldTime = LocalDateTime.now(ZoneOffset.UTC).minusHours(25);
    MentorAdvice.Kpt kpt = new MentorAdvice.Kpt("keep", "problem", "tryNext");
    MentorAdvice mentorAdvice =
        MentorAdvice.builder()
            .id("1")
            .userId("user123")
            .goalId("goal456")
            .isChecked(false)
            .message("message")
            .kpt(kpt)
            .updatedAt(oldTime)
            .build();

    // when
    boolean result = mentorAdvice.shouldFetch();

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("shouldFetch - 24시간 미만인 경우 false 반환")
  void shouldFetch_whenLessThan24Hours_returnFalse() {
    // given
    LocalDateTime recentTime = LocalDateTime.now(ZoneOffset.UTC).minusHours(23);
    MentorAdvice.Kpt kpt = new MentorAdvice.Kpt("keep", "problem", "tryNext");
    MentorAdvice mentorAdvice =
        MentorAdvice.builder()
            .id("1")
            .userId("user123")
            .goalId("goal456")
            .isChecked(false)
            .message("message")
            .kpt(kpt)
            .updatedAt(recentTime)
            .build();

    // when
    boolean result = mentorAdvice.shouldFetch();

    // then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("shouldFetch - 24시간에 가까운 경우 false 반환")
  void shouldFetch_whenExactly24Hours_returnFalse() {
    // given
    LocalDateTime nearExactTime = LocalDateTime.now(ZoneOffset.UTC).minusHours(24).plusMinutes(1);
    MentorAdvice.Kpt kpt = new MentorAdvice.Kpt("keep", "problem", "tryNext");
    MentorAdvice mentorAdvice =
        MentorAdvice.builder()
            .id("1")
            .userId("user123")
            .goalId("goal456")
            .isChecked(false)
            .message("message")
            .kpt(kpt)
            .updatedAt(nearExactTime)
            .build();

    // when
    boolean result = mentorAdvice.shouldFetch();

    // then
    assertThat(result).isFalse();
  }
}
