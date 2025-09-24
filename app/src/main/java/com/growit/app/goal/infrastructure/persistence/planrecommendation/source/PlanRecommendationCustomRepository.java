package com.growit.app.goal.infrastructure.persistence.planrecommendation.source;

import com.growit.app.goal.domain.planrecommendation.dto.FindPlanRecommendationCommand;
import com.growit.app.goal.infrastructure.persistence.planrecommendation.source.entity.PlanRecommendationEntity;
import java.util.Optional;

public interface PlanRecommendationCustomRepository {

  Optional<PlanRecommendationEntity> findByCommand(FindPlanRecommendationCommand command);
}
