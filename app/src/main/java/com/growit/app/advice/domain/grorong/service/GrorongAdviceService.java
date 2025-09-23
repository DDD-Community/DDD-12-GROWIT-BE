package com.growit.app.advice.domain.grorong.service;

import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.domain.grorong.vo.Mood;
import com.growit.app.user.domain.userstats.UserStats;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrorongAdviceService {

  public Grorong generateAdvice(UserStats userStats, String saying) {
    Mood mood = calculateMood(userStats);
    return Grorong.of(saying, mood);
  }

  private Mood calculateMood(UserStats userStats) {
    UserStats.AccessStatus status = userStats.getAccessStatus(LocalDate.now());
    return switch (status) {
      case THREE_DAYS_OR_MORE -> Mood.HAPPY;
      case NOT_ACCESSED_FOR_THREE_DAYS -> Mood.SAD;
      default -> Mood.NORMAL;
    };
  }
}
