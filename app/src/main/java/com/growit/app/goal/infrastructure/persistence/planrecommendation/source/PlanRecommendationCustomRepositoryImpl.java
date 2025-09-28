package com.growit.app.goal.infrastructure.persistence.planrecommendation.source;

import static com.growit.app.goal.infrastructure.persistence.planrecommendation.source.entity.QPlanRecommendationEntity.planRecommendationEntity;

import com.growit.app.goal.domain.planrecommendation.dto.FindPlanRecommendationCommand;
import com.growit.app.goal.infrastructure.persistence.planrecommendation.source.entity.PlanRecommendationEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlanRecommendationCustomRepositoryImpl implements PlanRecommendationCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<PlanRecommendationEntity> findByCommand(FindPlanRecommendationCommand command) {
    BooleanBuilder builder = new BooleanBuilder();

    builder.and(planRecommendationEntity.userId.eq(command.userId()));
    builder.and(planRecommendationEntity.goalId.eq(command.goalId()));

    if (command.planId() != null) {
      builder.and(planRecommendationEntity.planId.eq(command.planId()));
    } else {
      builder.and(planRecommendationEntity.planId.isNull());
    }

    return Optional.ofNullable(
        queryFactory.selectFrom(planRecommendationEntity).where(builder).fetchOne());
  }
}
