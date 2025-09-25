package com.growit.app.goal.infrastructure.persistence.planrecommendation.source;

import com.growit.app.goal.infrastructure.persistence.planrecommendation.source.entity.PlanRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRecommendationJpaRepository
    extends JpaRepository<PlanRecommendationEntity, Long>, PlanRecommendationCustomRepository {}
