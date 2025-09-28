package com.growit.app.user.infrastructure.persistence.promotion;

import com.growit.app.user.domain.promotion.Promotion;
import com.growit.app.user.domain.promotion.PromotionRepository;
import com.growit.app.user.infrastructure.persistence.promotion.source.PromotionQueryRepository;
import com.growit.app.user.infrastructure.persistence.promotion.source.entity.PromotionEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PromotionRepositoryImpl implements PromotionRepository {

  private final PromotionQueryRepository promotionQueryRepository;

  @Override
  public Optional<Promotion> findByCode(String code) {
    return promotionQueryRepository.findByCode(code).map(PromotionEntity::toDomain);
  }

  @Override
  public void save(Promotion promotion) {
    Optional<PromotionEntity> existingEntityOpt =
        promotionQueryRepository.findByUid(promotion.getId());

    if (existingEntityOpt.isPresent()) {
      // 기존 엔티티 업데이트
      PromotionEntity existingEntity = existingEntityOpt.get();
      existingEntity.updateByDomain(promotion);
      promotionQueryRepository.save(existingEntity);
    } else {
      // 새로운 엔티티 생성
      PromotionEntity entity = PromotionEntity.from(promotion);
      promotionQueryRepository.save(entity);
    }
  }

  @Override
  public Optional<Promotion> findById(String id) {
    return promotionQueryRepository.findByUid(id).map(PromotionEntity::toDomain);
  }
}
