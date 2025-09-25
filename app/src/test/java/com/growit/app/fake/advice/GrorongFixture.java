package com.growit.app.fake.advice;

import com.growit.app.advice.controller.dto.response.GrorongAdviceResponse;
import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.domain.grorong.vo.Mood;

public class GrorongFixture {
  public static Grorong defaultGrorong() {
    return Grorong.of("오늘도 화이팅!", Mood.HAPPY);
  }

  public static Grorong happyGrorong() {
    return Grorong.of("역시 넌 나를 실망시키지 않아", Mood.HAPPY);
  }

  public static Grorong normalGrorong() {
    return Grorong.of("꾸준히 나와주는 네 마음이야", Mood.NORMAL);
  }

  public static Grorong sadGrorong() {
    return Grorong.of("오늘은 왜 이리 안와…?", Mood.SAD);
  }

  public static GrorongAdviceResponse defaultGrorongAdviceResponse() {
    return new GrorongAdviceResponse(defaultGrorong());
  }
}
