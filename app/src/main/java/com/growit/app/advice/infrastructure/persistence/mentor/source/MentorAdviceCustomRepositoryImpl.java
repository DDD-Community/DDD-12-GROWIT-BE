package com.growit.app.advice.infrastructure.persistence.mentor.source;

import static com.growit.app.advice.infrastructure.persistence.mentor.source.entity.QMentorAdviceEntity.mentorAdviceEntity;
import static com.growit.app.advice.infrastructure.persistence.mentor.source.entity.QMentorAdviceKPTEntity.mentorAdviceKPTEntity;

import com.growit.app.advice.infrastructure.persistence.mentor.source.entity.MentorAdviceEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MentorAdviceCustomRepositoryImpl implements MentorAdviceCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<MentorAdviceEntity> findByUserIdAndGoalId(String userId, String goalId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(mentorAdviceEntity)
            .leftJoin(mentorAdviceEntity.kpt, mentorAdviceKPTEntity)
            .fetchJoin()
            .where(
                mentorAdviceEntity
                    .userId
                    .eq(userId)
                    .and(mentorAdviceEntity.goalId.eq(goalId)))
            .fetchOne());
  }

  @Override
  public List<MentorAdviceEntity> findByUserId(String userId) {
    return queryFactory
        .selectFrom(mentorAdviceEntity)
        .leftJoin(mentorAdviceEntity.kpt, mentorAdviceKPTEntity)
        .fetchJoin()
        .where(mentorAdviceEntity.userId.eq(userId))
        .fetch();
  }
}
