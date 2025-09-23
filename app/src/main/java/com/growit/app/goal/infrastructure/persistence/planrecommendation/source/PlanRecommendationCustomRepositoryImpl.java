package com.growit.app.goal.infrastructure.persistence.planrecommendation.source;

import static com.growit.app.goal.infrastructure.persistence.planrecommendation.source.entity.QPlanRecommendationEntity.planRecommendationEntity;

import com.growit.app.goal.domain.planrecommendation.dto.FindPlanRecommendationCommand;
import com.growit.app.goal.infrastructure.persistence.planrecommendation.source.entity.PlanRecommendationEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlanRecommendationCustomRepositoryImpl implements PlanRecommendationCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PlanRecommendationEntity> findByCommand(FindPlanRecommendationCommand command) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(planRecommendationEntity)
                        .where(planRecommendationEntity.userId.eq(command.userId())
                                .and(planRecommendationEntity.goalId.eq(command.goalId()))
                                .and(planRecommendationEntity.planId.eq(command.planId())))
                        .fetchOne()
        );
    }
}