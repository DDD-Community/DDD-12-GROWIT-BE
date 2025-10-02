package com.growit.app.user.infrastructure.persistence.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.infrastructure.persistence.promotion.source.PromotionQueryRepository;
import com.growit.app.user.infrastructure.persistence.promotion.source.entity.PromotionEntity;
import com.growit.app.user.infrastructure.persistence.user.source.DBUserRepository;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserPromotionMapEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserDBMapper userDBMapper;
  private final DBUserRepository dbUserRepository;
  private final PromotionQueryRepository promotionQueryRepository;

  @Override
  public Optional<User> findByEmail(Email email) {
    return dbUserRepository.findByEmail(email.value()).map(userDBMapper::toDomain);
  }

  @Override
  public Optional<User> findUserByuId(String id) {
    return dbUserRepository.findByUid(id).map(userDBMapper::toDomain);
  }

  @Override
  public void saveUser(User user) {
    Optional<UserEntity> userEntityOpt = dbUserRepository.findByUid(user.getId());
    UserEntity userEntity = null;
    if (userEntityOpt.isPresent()) {
      userEntity = userEntityOpt.get();
      userEntity.updateByDomain(user);
      dbUserRepository.save(userEntity);
    } else {
      userEntity = userDBMapper.toEntity(user);
      dbUserRepository.save(userEntity);
    }

    // UserPromotionMap 처리 - userId와 promotionId로 매핑
    syncUserPromotions(userEntity, user);
  }

  private void syncUserPromotions(UserEntity userEntity, User user) {

    if (user.getPromotion() != null) {
      PromotionEntity promotionEntity =
          promotionQueryRepository
              .findByUid(user.getPromotion().getId())
              .orElseThrow(
                  () ->
                      new RuntimeException("Promotion not found: " + user.getPromotion().getId()));

      // 기존에 같은 프로모션이 있는지 확인
      UserPromotionMapEntity existingMapping =
          userEntity.getUserPromotions().stream()
              .filter(mapping -> mapping.getPromotion().getUid().equals(promotionEntity.getUid()))
              .findFirst()
              .orElse(null);

      if (existingMapping == null) {
        // 새로운 매핑 생성
        UserPromotionMapEntity userPromotionMap =
            UserPromotionMapEntity.create(userEntity, promotionEntity);
        userEntity.getUserPromotions().add(userPromotionMap);
      }
      // 이미 존재하면 아무것도 하지 않음 (업데이트할 필요가 없음)
    }

    dbUserRepository.save(userEntity);
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    Page<UserEntity> page = dbUserRepository.findAll(pageable);
    List<User> content = page.getContent().stream().map(userDBMapper::toDomain).toList();
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }
}
