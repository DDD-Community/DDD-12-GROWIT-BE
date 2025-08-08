package com.growit.app.goal.infrastructure.persistence.goal.source.entity;

import static java.util.stream.Collectors.toMap;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.goal.domain.goal.Goal;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.*;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, length = 128)
  private String userId;

  @Column(nullable = false, length = 128)
  private String name;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false, length = 128)
  private String toBe;

  @OneToMany(
      mappedBy = "goal",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @Builder.Default
  private List<PlanEntity> plans = new ArrayList<>();

  public void updateToByDomain(Goal goal) {
    this.name = goal.getName();
    this.startDate = goal.getDuration().startDate();
    this.endDate = goal.getDuration().endDate();
    this.toBe = goal.getToBe();
    // 다르면 insert
    Map<String, PlanEntity> existingPlanMap =
        this.plans.stream().collect(toMap(PlanEntity::getUid, plan -> plan));

    List<PlanEntity> updatedPlans =
        goal.getPlans().stream()
            .map(
                plan -> {
                  PlanEntity existing = existingPlanMap.get(plan.getId());
                  if (existing != null) {
                    existing.updateByDomain(plan);
                    return existing;
                  } else {
                    return new PlanEntity(
                        plan.getId(),
                        plan.getWeekOfMonth(),
                        plan.getContent(),
                        plan.getPlanDuration().startDate(),
                        plan.getPlanDuration().endDate(),
                        this);
                  }
                })
            .toList();
    this.plans.clear();
    this.plans.addAll(updatedPlans);
    if (goal.getDeleted()) setDeletedAt(LocalDateTime.now());
  }
}
