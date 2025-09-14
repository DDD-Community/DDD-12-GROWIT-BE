package com.growit.app.fake.mentor;

import com.growit.app.mentor.domain.vo.IntimacyLevel;

public class MentorFixture {

  public static IntimacyLevel highIntimacyLevel() {
    return IntimacyLevel.HIGH;
  }

  public static IntimacyLevel mediumIntimacyLevel() {
    return IntimacyLevel.MEDIUM;
  }

  public static IntimacyLevel lowIntimacyLevel() {
    return IntimacyLevel.LOW;
  }

  public static String defaultUserId() {
    return "user-1";
  }
}
