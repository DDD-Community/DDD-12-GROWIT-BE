package com.growit.app.goal.domain.planrecommendation;

import com.growit.app.goal.domain.planrecommendation.dto.FindPlanRecommendationCommand;
import java.util.Optional;

public interface PlanRecommendationRepository {

  Optional<PlanRecommendation> findByCommand(FindPlanRecommendationCommand command);

  void save(PlanRecommendation planRecommendation);
}
