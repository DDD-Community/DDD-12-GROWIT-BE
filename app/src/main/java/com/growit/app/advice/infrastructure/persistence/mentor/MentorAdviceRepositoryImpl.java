package com.growit.app.advice.infrastructure.persistence.mentor;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.advice.infrastructure.persistence.mentor.source.MentorAdviceJpaRepository;
import com.growit.app.advice.infrastructure.persistence.mentor.source.entity.MentorAdviceEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MentorAdviceRepositoryImpl implements MentorAdviceRepository {

  private final MentorAdviceJpaRepository mentorAdviceJpaRepository;
  private final MentorAdviceDBMapper mentorAdviceDBMapper;

  @Override
  public Optional<MentorAdvice> findByUserIdAndGoalId(String userId, String goalId) {
    return mentorAdviceJpaRepository
        .findByUserIdAndGoalId(userId, goalId)
        .map(mentorAdviceDBMapper::toDomain);
  }

  @Override
  public void save(MentorAdvice mentorAdvice) {
    mentorAdviceJpaRepository
        .findByUserIdAndGoalId(mentorAdvice.getUserId(), mentorAdvice.getGoalId())
        .ifPresentOrElse(
            entity -> {
              entity.updateByDomain(mentorAdvice);
              mentorAdviceJpaRepository.save(entity);
            },
            () -> {
              MentorAdviceEntity entity = mentorAdviceDBMapper.toEntity(mentorAdvice);
              mentorAdviceJpaRepository.save(entity);
            });
  }
}
