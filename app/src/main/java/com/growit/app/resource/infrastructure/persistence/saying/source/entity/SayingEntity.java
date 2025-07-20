package com.growit.app.resource.infrastructure.persistence.saying.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.resource.domain.saying.Saying;
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

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(length = 256, nullable = false)
  private String message;

  @Column(length = 32, nullable = false)
  private String author;

  public SayingEntity(String uid, String message, String author) {
    this.uid = uid;
    this.message = message;
    this.author = author;
  }

  public void updateByDomain(Saying saying) {
    this.message = saying.getMessage();
    this.author = saying.getAuthor();
  }
}
