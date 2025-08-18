package com.growit.app.goal.infrastructure.persistence.goal.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.GoalCategory;
import com.growit.app.goal.domain.goal.vo.GoalUpdateStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private GoalCategory category = GoalCategory.UNCATEGORIZED;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private GoalUpdateStatus updateStatus = GoalUpdateStatus.UPDATABLE;

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
    this.category = goal.getCategory();
    this.updateStatus = goal.getUpdateStatus();

    if (goal.getPlans() != null && !goal.getPlans().isEmpty()) {
      Map<String, String> contentById =
          goal.getPlans().stream()
              .filter(p -> p.getId() != null)
              .collect(Collectors.toMap(Plan::getId, Plan::getContent, (a, b) -> b));
      for (PlanEntity pe : this.plans) {
        String newContent = contentById.get(pe.getUid());
        if (newContent != null && !Objects.equals(pe.getContent(), newContent)) {
          pe.setContent(newContent);
        }
      }
    }

    if (goal.getDeleted()) setDeletedAt(LocalDateTime.now());
  }
}
