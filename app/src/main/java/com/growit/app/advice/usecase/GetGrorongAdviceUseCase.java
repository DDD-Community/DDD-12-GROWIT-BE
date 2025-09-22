package com.growit.app.advice.usecase;

import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import com.growit.app.resource.usecase.GetSayingUseCase;
import com.growit.app.user.domain.userstats.UserStats;
import com.growit.app.advice.domain.grorong.service.GrorongAdviceService;
import com.growit.app.user.domain.userstats.UserStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetGrorongAdviceUseCase {
    private final GetSayingUseCase getSayingUseCase;
    private final UserStatsRepository userStatsRepository;
    private final GrorongAdviceService grorongAdviceService;

    public Grorong execute(String userId) {
        UserStats userStats = userStatsRepository.findByUserId(userId);
        String saying = getSayingUseCase.execute().getMessage();

        return grorongAdviceService.generateAdvice(userStats, saying);
    }
}
