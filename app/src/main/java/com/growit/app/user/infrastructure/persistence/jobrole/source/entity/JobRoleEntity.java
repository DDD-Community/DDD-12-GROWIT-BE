package com.growit.app.user.infrastructure.persistence.jobrole.source.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
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

  @PrePersist
  public void ensureJobRoleId() {
    if (this.uid == null || this.uid.isBlank()) {
      this.uid = NanoIdUtils.randomNanoId();
    }
  }

  @Builder
  public JobRoleEntity(String jobRoleId, String name) {
    this.uid = jobRoleId;
    this.name = name;
  }
}
