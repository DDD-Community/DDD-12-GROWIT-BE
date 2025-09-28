package com.growit.app.user.domain.promotion;

import java.util.Optional;

public interface PromotionRepository {
  Optional<Promotion> findByCode(String code);

  void save(Promotion promotion);

  Optional<Promotion> findById(String id);
}
