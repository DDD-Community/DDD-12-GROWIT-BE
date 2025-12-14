package com.growit.app.goal.infrastructure.persistence.goal.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.goal.domain.goal.vo.Planet;
import jakarta.persistence.*;
import java.time.LocalDate;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "planet_id")
  private PlanetEntity planet;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private GoalStatus status = GoalStatus.IN_PROGRESS;

  public Goal toDomain() {
    Planet domainPlanet = planet.toDomain();
    GoalDuration duration = new GoalDuration(startDate, endDate);
    Goal goal = Goal.create(uid, name, userId, domainPlanet, duration);
    
    // Status 설정
    if (status == GoalStatus.COMPLETED) {
      goal.complete();
    }
    
    return goal;
  }

  public static GoalEntity fromDomain(Goal goal, String userId, PlanetEntity planetEntity) {
    GoalDuration duration = goal.getDuration();
    
    return GoalEntity.builder()
        .uid(goal.getId())
        .userId(userId)
        .name(goal.getName())
        .startDate(duration.startDate())
        .endDate(duration.endDate())
        .planet(planetEntity)
        .status(goal.getStatus())
        .build();
  }

  public void updateFromDomain(Goal goal) {
    this.name = goal.getName();
    this.startDate = goal.getDuration().startDate();
    this.endDate = goal.getDuration().endDate();
    this.status = goal.getStatus();
  }
}
