package com.growit.app.retrospect.infrastructure.persistence.retrospect.source;

import static com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.QRetrospectEntity.retrospectEntity;

import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBRetrospectQueryRepositoryImpl implements DBRetrospectQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<RetrospectEntity> findByUidAndUserId(String uid, String userId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(retrospectEntity)
            .where(
                retrospectEntity.uid.eq(uid),
                retrospectEntity.userId.eq(userId),
                retrospectEntity.deletedAt.isNull())
            .fetchOne());
  }

  @Override
  public Optional<RetrospectEntity> findByPlanId(String planId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(retrospectEntity)
            .where(retrospectEntity.planId.eq(planId), retrospectEntity.deletedAt.isNull())
            .fetchOne());
  }

  @Override
  public Optional<RetrospectEntity> findByFilter(RetrospectQueryFilter filter) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(retrospectEntity)
            .where(
                retrospectEntity.goalId.eq(filter.goalId()),
                retrospectEntity.planId.eq(filter.planId()),
                retrospectEntity.userId.eq(filter.userId()),
                retrospectEntity.deletedAt.isNull())
            .fetchOne());
  }

  @Override
  public List<RetrospectEntity> findByGoalIdAndUserId(String goalId, String userId) {
    return queryFactory
        .selectFrom(retrospectEntity)
        .where(
            retrospectEntity.goalId.eq(goalId),
            retrospectEntity.userId.eq(userId),
            retrospectEntity.deletedAt.isNull())
        .fetch();
  }

  @Override
  public int countWeeklyRetrospectsByUserId(String userId) {
    Long count =
        queryFactory
            .select(retrospectEntity.count())
            .from(retrospectEntity)
            .where(retrospectEntity.userId.eq(userId), retrospectEntity.deletedAt.isNull())
            .fetchOne();

    return count != null ? count.intValue() : 0;
  }
}
