package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.QGoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.QPlanEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBGoalQueryRepositoryImpl implements DBGoalQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<GoalEntity> findByUserId(String userId) {
    QGoalEntity goal = QGoalEntity.goalEntity;
    QPlanEntity plan = QPlanEntity.planEntity;
    return queryFactory
        .selectFrom(goal)
        .leftJoin(goal.plans, plan)
        .fetchJoin()
        .where(goal.userId.eq(userId), goal.deletedAt.isNull())
        .fetch();
  }

  //  @Override
  //  public PlanEntity findByPlanId(String planId) {
  //    QPlanEntity plan = QPlanEntity.planEntity;
  //    return queryFactory.selectFrom(plan).where(plan.uid.eq(planId)).fetchFirst();
  //  }
}
