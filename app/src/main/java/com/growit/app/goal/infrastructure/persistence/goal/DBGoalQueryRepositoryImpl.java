package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.infrastructure.persistence.goal.source.DBGoalQueryRepository;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.QGoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.QPlanEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DBGoalQueryRepositoryImpl implements DBGoalQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<GoalEntity> findWithPlansByUid(String uid) {
    QGoalEntity goal = QGoalEntity.goalEntity;
    QPlanEntity plan = QPlanEntity.planEntity;


    GoalEntity entity = queryFactory.selectFrom(goal).leftJoin(goal.plans, plan).fetchJoin()
      .where(goal.uid.eq(uid)).fetchOne();
    return Optional.ofNullable(entity);
  }
}
