package com.growit.app.goal.infrastructure.persistence.goal.retrospect.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "retrospects", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"goalId", "planId"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetrospectEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false)
  private String goalId;

  @Column(nullable = false)
  private String planId;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;
}