package com.growit.app.mission.infrastructure.persistence.mission.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "missions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionEntity extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false)
  private boolean finished;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false, length = 128)
  private String userId;
}
