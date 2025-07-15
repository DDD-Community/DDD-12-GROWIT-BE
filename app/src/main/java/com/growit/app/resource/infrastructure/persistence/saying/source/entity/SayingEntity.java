package com.growit.app.resource.infrastructure.persistence.saying.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "sayings")
@Entity
@NoArgsConstructor
public class SayingEntity extends BaseEntity {

  @Column(length = 256, nullable = false)
  private String message;

  @Column(length = 32, nullable = false)
  private String author;
}
