package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.domain.goal.vo.GoalUpdateStatus;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.QGoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.QPlanEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBGoalQueryRepositoryImpl implements DBGoalQueryRepository {

  private final JPAQueryFactory queryFactory;
  @PersistenceContext private final EntityManager em;

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

  @Override
  public Optional<GoalEntity> findByUidAndUserId(String uid, String userId) {
    QGoalEntity goal = QGoalEntity.goalEntity;
    QPlanEntity plan = QPlanEntity.planEntity;
    return Optional.ofNullable(
        queryFactory
            .selectFrom(goal)
            .leftJoin(goal.plans, plan)
            .fetchJoin()
            .where(goal.uid.eq(uid), goal.userId.eq(userId), goal.deletedAt.isNull())
            .fetchOne());
  }

  @Override
  public Optional<GoalEntity> findByUserIdAndGoalDuration(String userId, LocalDate today) {
    QGoalEntity goal = QGoalEntity.goalEntity;

    return Optional.ofNullable(
        queryFactory
            .selectFrom(goal)
            .where(
                goal.userId.eq(userId),
                goal.deletedAt.isNull(),
                goal.startDate.loe(today),
                goal.endDate.goe(today))
            .fetchOne());
  }

  @Override
  public List<String> findEndedCandidateUids(
      LocalDate today, GoalUpdateStatus updateStatus, PageRequest pageRequest) {
    QGoalEntity goal = QGoalEntity.goalEntity;
    return queryFactory
        .select(goal.uid)
        .from(goal)
        .where(
            goal.deletedAt.isNull(), goal.updateStatus.ne(updateStatus), goal.endDate.before(today))
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize())
        .fetch();
  }

  @Override
  public List<GoalEntity> findByUidIn(List<String> uids) {
    if (uids == null || uids.isEmpty()) return List.of();
    QGoalEntity goal = QGoalEntity.goalEntity;

    return queryFactory.selectFrom(goal).where(goal.uid.in(uids)).fetch();
  }

  @Override
  public void flushAndClear() {
    em.flush();
    em.clear();
  }
}
