package com.growit.app.goal.infrastructure.persistence.planrecommendation;

import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendationRepository;
import com.growit.app.goal.domain.planrecommendation.dto.FindPlanRecommendationCommand;
import com.growit.app.goal.infrastructure.persistence.planrecommendation.source.PlanRecommendationJpaRepository;
import com.growit.app.goal.infrastructure.persistence.planrecommendation.source.entity.PlanRecommendationEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlanRecommendationRepositoryImpl implements PlanRecommendationRepository {

  private final PlanRecommendationJpaRepository planRecommendationJpaRepository;

  @Override
  public Optional<PlanRecommendation> findByCommand(FindPlanRecommendationCommand command) {
    return planRecommendationJpaRepository
        .findByCommand(command)
        .map(PlanRecommendationEntity::toDomain);
  }

  @Override
  public void save(PlanRecommendation planRecommendation) {
    FindPlanRecommendationCommand command =
        new FindPlanRecommendationCommand(
            planRecommendation.getUserId(),
            planRecommendation.getGoalId(),
            planRecommendation.getPlanId());

    planRecommendationJpaRepository
        .findByCommand(command)
        .ifPresentOrElse(
            entity -> {
              entity.updateByDomain(planRecommendation);
              planRecommendationJpaRepository.save(entity);
            },
            () -> {
              PlanRecommendationEntity entity = PlanRecommendationEntity.from(planRecommendation);
              planRecommendationJpaRepository.save(entity);
            });
  }
}
