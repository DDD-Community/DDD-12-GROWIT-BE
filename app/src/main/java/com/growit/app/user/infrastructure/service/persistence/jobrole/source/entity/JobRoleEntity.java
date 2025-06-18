package com.growit.app.user.infrastructure.service.persistence.jobrole.source.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "jobrole")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class JobRoleEntity {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID")
  private UUID id;

  @Column(length = 32, nullable = false)
  private String name;

  @Builder
  public JobRoleEntity(String id, String name) {
    this.id = UUID.fromString(id);
    this.name = name;
  }
}
