package com.growit.app.user.infrastructure.persistence.promotion.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.promotion.Promotion;
import com.growit.app.user.domain.promotion.PromotionType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "promotions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, unique = true)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PromotionType type;

  @Column(name = "expired_at", nullable = false)
  private LocalDateTime expiredAt;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @Column(name = "is_used", nullable = false)
  private Boolean isUsed;

  public static PromotionEntity from(Promotion promotion) {
    return PromotionEntity.builder()
        .uid(promotion.getId())
        .code(promotion.getCode())
        .type(promotion.getType())
        .expiredAt(promotion.getExpiredAt())
        .isActive(promotion.isActive())
        .isUsed(promotion.isUsed())
        .build();
  }

  public Promotion toDomain() {
    return Promotion.builder()
        .id(this.uid)
        .code(this.code)
        .type(this.type)
        .expiredAt(this.expiredAt)
        .isActive(this.isActive)
        .isUsed(this.isUsed)
        .build();
  }

  public void updateByDomain(Promotion promotion) {
    this.code = promotion.getCode();
    this.type = promotion.getType();
    this.expiredAt = promotion.getExpiredAt();
    this.isActive = promotion.isActive();
    this.isUsed = promotion.isUsed();
  }
}
