package com.growit.app.resource.infrastructure.persistence.jobrole.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "jobrole")
@Entity
@NoArgsConstructor
public class JobRoleEntity extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String uid;

  @Column(length = 32, nullable = false)
  private String name;
}
