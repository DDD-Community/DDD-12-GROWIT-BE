package com.growit.app.user.infrastructure.persistence.promotion.source;

import com.growit.app.user.infrastructure.persistence.promotion.source.entity.PromotionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionQueryRepository extends JpaRepository<PromotionEntity, Long> {
  Optional<PromotionEntity> findByCode(String code);

  Optional<PromotionEntity> findByUid(String uid);
}
