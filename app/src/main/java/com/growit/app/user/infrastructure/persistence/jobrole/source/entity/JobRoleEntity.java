package com.growit.app.user.infrastructure.persistence.jobrole.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name="jobrole")
@Entity
@NoArgsConstructor
public class JobRoleEntity extends BaseEntity {
  private String id;
  private String name;

  @Builder
  public JobRoleEntity(String id, String name) {
    this.id = id;
    this.name = name;
  }

}
