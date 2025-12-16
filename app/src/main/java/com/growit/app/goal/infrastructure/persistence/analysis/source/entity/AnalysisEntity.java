package com.growit.app.goal.infrastructure.persistence.analysis.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goal_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String goalId;

  @Column(nullable = false)
  private int todoCompletedRate;

  @Column(nullable = false, length = 500)
  private String summary;

  public GoalAnalysis toDomain() {
    return GoalAnalysis.of(todoCompletedRate, summary);
  }

  public static AnalysisEntity fromDomain(String goalId, GoalAnalysis analysis) {
    return AnalysisEntity.builder()
        .goalId(goalId)
        .todoCompletedRate(analysis.todoCompletedRate())
        .summary(analysis.summary())
        .build();
  }

  public void updateFromDomain(GoalAnalysis analysis) {
    this.todoCompletedRate = analysis.todoCompletedRate();
    this.summary = analysis.summary();
  }
}
