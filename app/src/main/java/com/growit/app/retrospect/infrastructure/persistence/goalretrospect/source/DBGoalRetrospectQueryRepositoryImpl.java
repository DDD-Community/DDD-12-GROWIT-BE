package com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source;

import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity.GoalRetrospectEntity;
import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity.QGoalRetrospectEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBGoalRetrospectQueryRepositoryImpl implements DBGoalRetrospectQueryRepository {
  private final JPAQueryFactory query;
  private final QGoalRetrospectEntity qGoalRetrospectEntity =
      QGoalRetrospectEntity.goalRetrospectEntity;

  @Override
  public List<GoalRetrospectEntity> findAllByGoalIdAndCreatedAtBetween(
      String goalId, LocalDateTime start, LocalDateTime end) {

    BooleanBuilder builder = new BooleanBuilder();

    if (goalId != null) {
      builder.and(qGoalRetrospectEntity.goalId.eq(goalId));
    }

    builder.and(qGoalRetrospectEntity.createdAt.between(start, end));
    builder.and(qGoalRetrospectEntity.deletedAt.isNull());

    return query
        .selectFrom(qGoalRetrospectEntity)
        .where(builder)
        .fetch();
  }
}
