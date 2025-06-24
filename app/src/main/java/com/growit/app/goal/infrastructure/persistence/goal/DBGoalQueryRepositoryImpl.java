package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.infrastructure.persistence.goal.source.DBGoalQueryRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.QGoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.QPlanEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBGoalQueryRepositoryImpl implements DBGoalQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<GoalEntity> findWithPlansByUserId(String userId) {
    QGoalEntity goal = QGoalEntity.goalEntity;
    QPlanEntity plan = QPlanEntity.planEntity;

    GoalEntity entity =
        queryFactory
            .selectFrom(goal)
            .leftJoin(goal.plans, plan)
            .fetchJoin()
            .where(goal.userId.eq(userId))
            .fetchOne();
    return Optional.ofNullable(entity);
  }
}
