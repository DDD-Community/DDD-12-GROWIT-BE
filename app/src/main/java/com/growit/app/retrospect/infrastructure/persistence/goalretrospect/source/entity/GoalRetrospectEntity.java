package com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goal_retrospects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalRetrospectEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false)
  private String goalId;

  @Column(nullable = false)
  private int todoCompletedRate;

  @Column(nullable = false, length = 1000)
  private String analysisSummary;

  @Column(nullable = false, length = 1000)
  private String analysisAdvice;

  @Column(nullable = false, length = 1000)
  private String content;

  public void updateByDomain(GoalRetrospect goalRetrospect) {
    this.analysisSummary = goalRetrospect.getAnalysis().summary();
    this.analysisAdvice = goalRetrospect.getAnalysis().advice();
    this.content = goalRetrospect.getContent();
  }
}
