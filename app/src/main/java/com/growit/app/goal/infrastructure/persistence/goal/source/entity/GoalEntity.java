package com.growit.app.goal.infrastructure.persistence.goal.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
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
}
