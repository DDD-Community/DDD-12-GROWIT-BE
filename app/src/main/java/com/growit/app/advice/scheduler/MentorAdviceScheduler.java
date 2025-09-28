package com.growit.app.advice.scheduler;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.advice.usecase.GenerateMentorAdviceUseCase;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorAdviceScheduler {

    private final GenerateMentorAdviceUseCase generateMentorAdviceUseCase;
    private final MentorAdviceRepository mentorAdviceRepository;
    private final UserRepository userRepository;

    /**
     * 매일 오전 0시에 오늘의 조언을 생성합니다.
     * cron = "초 분 시 일 월 요일"
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void generateDailyMentorAdvice() {
        log.info("데일리 멘토 조언 생성 스케줄러 시작");

        Pageable pageable = PageRequest.of(0, 100);
        Page<User> userPage;

        do {
            userPage = userRepository.findAll(pageable);
            for (User user : userPage.getContent()) {
                try {
                    MentorAdvice mentorAdvice = generateMentorAdviceUseCase.execute(user);
                    mentorAdviceRepository.save(mentorAdvice);
                    log.info("사용자 '{}'의 조언 생성 완료", user.getId());
                } catch (Exception e) {
                    log.error("사용자 '{}'의 조언 생성 중 오류 발생: {}", user.getId(), e.getMessage());
                }
            }
            pageable = userPage.nextPageable();
        } while (userPage.hasNext());

        log.info("데일리 멘토 조언 생성 스케줄러 종료");
    }
}
