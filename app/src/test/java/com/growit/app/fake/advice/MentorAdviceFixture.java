package com.growit.app.fake.advice;

import com.growit.app.advice.controller.dto.response.MentorAdviceResponse;
import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.domain.grorong.vo.Mood;
import com.growit.app.advice.domain.mentor.MentorAdvice;

public class MentorAdviceFixture {

  public static MentorAdvice defaultMentorAdvice() {
    return new MentorAdviceBuilder().build();
  }

  public static MentorAdvice checkedMentorAdvice() {
    return new MentorAdviceBuilder().withIsChecked(true).build();
  }

  public static MentorAdvice uncheckedMentorAdvice() {
    return new MentorAdviceBuilder().withIsChecked(false).build();
  }

  public static MentorAdviceResponse defaultMentorAdviceResponse() {
    return new MentorAdviceResponse(
        true,
        "혁신은 단순함에서 시작돼",
        new MentorAdviceResponse.KptResponse(
            "투두 분해와 실행 리듬은 안정적이다.", "디자인 - 개발 QA 협업 속도가 더디다", "이번주는 MVP를 반드시 배포해라."));
  }

  public static MentorAdvice.Kpt defaultKpt() {
    return new MentorAdvice.Kpt(
        "투두 분해와 실행 리듬은 안정적이다.", "디자인 - 개발 QA 협업 속도가 더디다", "이번주는 MVP를 반드시 배포해라.");
  }

  // Grorong 관련 fixtures
  public static Grorong defaultGrorong() {
    return Grorong.of("최고의 아이디어는 종종 테이블 위의 커피잔 옆에서 나온다!", Mood.HAPPY);
  }

  public static Grorong happyGrorong() {
    return Grorong.of("당신의 노력이 빛을 발하고 있어요!", Mood.HAPPY);
  }

  public static Grorong normalGrorong() {
    return Grorong.of("꾸준히 하다보면 좋은 일이 생길 거예요!", Mood.NORMAL);
  }

  public static Grorong sadGrorong() {
    return Grorong.of("요즘은 왜 이리 안와...?", Mood.SAD);
  }
}

class MentorAdviceBuilder {
  private String id = "mentor-advice-1";
  private String userId = "user-1";
  private String goalId = "goal-1";
  private boolean isChecked = false;
  private String message = "혁신은 단순함에서 시작돼";
  private MentorAdvice.Kpt kpt = MentorAdviceFixture.defaultKpt();

  public MentorAdviceBuilder withId(String id) {
    this.id = id;
    return this;
  }

  public MentorAdviceBuilder withUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public MentorAdviceBuilder withGoalId(String goalId) {
    this.goalId = goalId;
    return this;
  }

  public MentorAdviceBuilder withIsChecked(boolean isChecked) {
    this.isChecked = isChecked;
    return this;
  }

  public MentorAdviceBuilder withMessage(String message) {
    this.message = message;
    return this;
  }

  public MentorAdviceBuilder withKpt(MentorAdvice.Kpt kpt) {
    this.kpt = kpt;
    return this;
  }

  public MentorAdvice build() {
    return new MentorAdvice(id, userId, goalId, isChecked, message, kpt);
  }
}
