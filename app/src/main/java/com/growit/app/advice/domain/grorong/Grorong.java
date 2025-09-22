package com.growit.app.advice.domain.grorong;

import com.growit.app.advice.domain.grorong.vo.Mood;
import com.growit.app.user.domain.userstats.UserStats;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Grorong {
    private final String saying;
    private final Mood mood;


    public static Grorong of(String saying, Mood mood) {
        return Grorong.builder()
                .saying(saying)
                .mood(mood)
                .build();
    }
}
