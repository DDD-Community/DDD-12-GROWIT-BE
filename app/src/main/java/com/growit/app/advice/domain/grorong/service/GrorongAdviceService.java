package com.growit.app.advice.domain.grorong.service;

import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.domain.grorong.vo.Mood;
import com.growit.app.user.domain.userstats.UserStats;
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
        if (userStats.streakLen() >= 3) {
            return Mood.HAPPY;
        } else if (userStats.streakLen() < 3) {
            return Mood.SAD;
        } else {
            return Mood.NORMAL;
        }
    }

}
