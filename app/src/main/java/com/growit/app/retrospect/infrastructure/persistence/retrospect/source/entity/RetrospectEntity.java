package com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "retrospects",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"goalId", "planId"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetrospectEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, length = 128)
  private String goalId;

  @Column(nullable = false, length = 128)
  private String planId;

  @Column(nullable = false, length = 500)
  private String content;

  public void updateByDomain(Retrospect retrospect) {
    this.content = retrospect.getContent();
  }
}
