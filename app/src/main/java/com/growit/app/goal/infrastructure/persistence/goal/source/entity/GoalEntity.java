package com.growit.app.goal.infrastructure.persistence.goal.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalUpdateStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(
    name = "goals",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_goals_user_id_end_date",
          columnNames = {"user_id", "end_date"})
    })
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

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private GoalUpdateStatus updateStatus = GoalUpdateStatus.UPDATABLE;

  // Plans relationship removed as plan domain has been deleted

  public void updateToByDomain(Goal goal) {
    this.name = goal.getName();
    this.startDate = goal.getDuration().startDate();
    this.endDate = goal.getDuration().endDate();
    this.updateStatus = goal.getUpdateStatus();
  }
}
