package com.growit.app.user.infrastructure.persistence.user.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.infrastructure.persistence.promotion.source.entity.PromotionEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_promotion_map")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPromotionMapEntity extends BaseEntity {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "uid", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "promotion_id", referencedColumnName = "uid", nullable = false)
  private PromotionEntity promotion;

  public static UserPromotionMapEntity create(UserEntity user, PromotionEntity promotion) {
    return UserPromotionMapEntity.builder().user(user).promotion(promotion).build();
  }
}
