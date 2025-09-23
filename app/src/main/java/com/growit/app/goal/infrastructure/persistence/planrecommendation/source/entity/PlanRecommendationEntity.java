package com.growit.app.goal.infrastructure.persistence.planrecommendation.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "plan_recommendations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "goal_id", "plan_id"})
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PlanRecommendationEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "goal_id", nullable = false, length = 36)
    private String goalId;

    @Column(name = "plan_id", nullable = false, length = 36)
    private String planId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    public static PlanRecommendationEntity from(PlanRecommendation planRecommendation) {
        return PlanRecommendationEntity.builder()
                .userId(planRecommendation.getUserId())
                .goalId(planRecommendation.getGoalId())
                .planId(planRecommendation.getPlanId())
                .content(planRecommendation.getContent())
                .build();
    }

    public PlanRecommendation toDomain() {
        return new PlanRecommendation(
                getId().toString(),
                userId,
                goalId,
                planId,
                content
        );
    }

    public void updateByDomain(PlanRecommendation planRecommendation) {
        this.planId = planRecommendation.getPlanId();
        this.content = planRecommendation.getContent();
    }
}
