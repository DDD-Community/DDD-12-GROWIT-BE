package com.growit.goal.infrastructure.persistence.goal.source.entity;

import com.growit.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false)
  private int weekOfMonth;

  @Column(nullable = false, length = 128)
  private String content;

  @ManyToOne
  @JoinColumn(name = "goal_id")
  private GoalEntity goal;
}
