package com.growit.app.goal.infrastructure.persistence.retrospect.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "retrospects")
@Getter
@Setter
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
}