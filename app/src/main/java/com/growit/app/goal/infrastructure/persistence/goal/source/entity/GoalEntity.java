package com.growit.app.goal.infrastructure.persistence.goal.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.goal.domain.goal.Goal;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
  private String asIs;

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
    this.asIs = goal.getBeforeAfter().asIs();
    this.toBe = goal.getBeforeAfter().toBe();
    // 기존 plan
    this.plans =
        goal.getPlans().stream()
            .map(
                plan ->
                    new PlanEntity(
                        plan.getId(),
                        plan.getWeekOfMonth(),
                        plan.getContent(),
                        plan.getPlanDuration().startDate(),
                        plan.getPlanDuration().endDate(),
                        this))
            .toList();

    if (goal.getDeleted()) setDeletedAt(LocalDateTime.now());
  }
}
