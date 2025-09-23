package com.growit.app.advice.infrastructure.persistence.mentor;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.advice.infrastructure.persistence.mentor.source.MentorAdviceJpaRepository;
import com.growit.app.advice.infrastructure.persistence.mentor.source.entity.MentorAdviceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MentorAdviceRepositoryImpl implements MentorAdviceRepository {

    private final MentorAdviceJpaRepository mentorAdviceJpaRepository;

    @Override
    public Optional<MentorAdvice> findByUserIdAndGoalId(String userId, String goalId) {
        return mentorAdviceJpaRepository.findByUserIdAndGoalId(userId, goalId)
                .map(MentorAdviceEntity::toDomain);
    }

    @Override
    public void save(MentorAdvice mentorAdvice) {
        mentorAdviceJpaRepository.findByUserIdAndGoalId(mentorAdvice.getUserId(), mentorAdvice.getGoalId())
                .ifPresentOrElse(
                        entity -> {
                            entity.updateByDomain(mentorAdvice);
                            mentorAdviceJpaRepository.save(entity);
                        },
                        () -> {
                            MentorAdviceEntity entity = MentorAdviceEntity.from(mentorAdvice);
                            mentorAdviceJpaRepository.save(entity);
                        }
                );
    }
}
