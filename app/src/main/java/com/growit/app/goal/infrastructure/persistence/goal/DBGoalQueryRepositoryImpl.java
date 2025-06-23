package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.infrastructure.persistence.goal.source.DBGoalQueryRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DBGoalQueryRepositoryImpl implements DBGoalQueryRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<GoalEntity> findWithPlansByUid(String uid) {
    //    QGoalEntity goal = QGoalEntity.goalEntity;
    //    QPlanEntity plan = QPlanEntity.planEntity;

    //    GoalEntity result = queryFactory
    //      .selectFrom(goal)
    //      .leftJoin(goal.plans, plan).fetchJoin()
    //      .where(goal.uid.eq(uid))
    //      .fetchOne();

    //    return Optional.ofNullable(result);

    return Optional.empty();
  }
}
