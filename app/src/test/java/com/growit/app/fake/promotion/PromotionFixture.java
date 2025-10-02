package com.growit.app.fake.promotion;

import com.growit.app.user.domain.promotion.Promotion;
import com.growit.app.user.domain.promotion.PromotionType;
import java.time.LocalDateTime;

public class PromotionFixture {

  public static Promotion validPromotion() {
    return validPromotionWithCode("WELCOME2024");
  }

  public static Promotion validPromotionWithCode(String code) {
    return Promotion.builder()
        .id("promo-id-1")
        .code(code)
        .type(PromotionType.AD_REMOVAL_ONE_YEAR)
        .expiredAt(LocalDateTime.now().plusDays(30))
        .isActive(true)
        .isUsed(false)
        .build();
  }

  public static Promotion expiredPromotion() {
    return expiredPromotionWithCode("EXPIRED_PROMO");
  }

  public static Promotion expiredPromotionWithCode(String code) {
    return Promotion.builder()
        .id("promo-id-2")
        .code(code)
        .type(PromotionType.AD_REMOVAL_ONE_YEAR)
        .expiredAt(LocalDateTime.now().minusDays(30))
        .isActive(false)
        .isUsed(false)
        .build();
  }

  public static Promotion usedPromotion() {
    return usedPromotionWithCode("USED_PROMO");
  }

  public static Promotion usedPromotionWithCode(String code) {
    return Promotion.builder()
        .id("promo-id-3")
        .code(code)
        .type(PromotionType.AD_REMOVAL_ONE_YEAR)
        .expiredAt(LocalDateTime.now().plusDays(30))
        .isActive(true)
        .isUsed(true)
        .build();
  }

  public static Promotion activePromotion() {
    return activePromotionWithCode("EXISTING_PROMO");
  }

  public static Promotion activePromotionWithCode(String code) {
    return Promotion.builder()
        .id("existing-promo-id")
        .code(code)
        .type(PromotionType.AD_REMOVAL_ONE_YEAR)
        .expiredAt(LocalDateTime.now().plusDays(30))
        .isActive(true)
        .isUsed(false)
        .build();
  }
}
