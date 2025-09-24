package com.growit.app.advice.domain.mentor;

import static org.assertj.core.api.Assertions.assertThat;

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
    MentorAdvice mentorAdvice = new MentorAdvice(id, userId, goalId, isChecked, message, kpt);

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
    MentorAdvice mentorAdvice = new MentorAdvice("1", "user123", "goal456", false, "message", kpt);

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
}
