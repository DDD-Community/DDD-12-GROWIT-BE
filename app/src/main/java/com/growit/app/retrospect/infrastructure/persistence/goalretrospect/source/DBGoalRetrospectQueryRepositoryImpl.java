package com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source;

import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity.GoalRetrospectEntity;
import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity.QGoalRetrospectEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DBGoalRetrospectQueryRepositoryImpl implements DBGoalRetrospectQueryRepository {
    private final JPAQueryFactory query;
    private final QGoalRetrospectEntity qGoalRetrospectEntity = QGoalRetrospectEntity.goalRetrospectEntity;

    @Override
    public List<GoalRetrospectEntity> findAllByGoalIdAndCreatedAtBetween(String goalId, LocalDateTime start, LocalDateTime end) {
        return query.selectFrom(qGoalRetrospectEntity)
                .where(qGoalRetrospectEntity.goalId.eq(goalId)
                        .and(qGoalRetrospectEntity.createdAt.between(start, end))
                        .and(qGoalRetrospectEntity.deletedAt.isNull()))
                .fetch();
    }
}
