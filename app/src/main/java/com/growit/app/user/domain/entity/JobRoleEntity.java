package com.growit.app.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "jobrole")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobRoleEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(length = 32, nullable = false)
  private String name;

  @Builder
  public JobRoleEntity(String name) {
    this.name = name;
  }
}
