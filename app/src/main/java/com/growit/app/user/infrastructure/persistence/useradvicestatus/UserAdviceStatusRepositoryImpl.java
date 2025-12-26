package com.growit.app.user.infrastructure.persistence.useradvicestatus;

import com.growit.app.user.domain.useradvicestatus.UserAdviceStatus;
import com.growit.app.user.domain.useradvicestatus.repository.UserAdviceStatusRepository;
import com.growit.app.user.infrastructure.persistence.useradvicestatus.source.UserAdviceStatusJpaRepository;
import com.growit.app.user.infrastructure.persistence.useradvicestatus.source.entity.UserAdviceStatusEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserAdviceStatusRepositoryImpl implements UserAdviceStatusRepository {

  private final UserAdviceStatusJpaRepository userAdviceStatusJpaRepository;

  @Override
  public Optional<UserAdviceStatus> findByUserId(String userId) {
    return userAdviceStatusJpaRepository.findByUserId(userId).map(UserAdviceStatusEntity::toDomain);
  }

  @Override
  public void save(UserAdviceStatus userAdviceStatus) {
    userAdviceStatusJpaRepository
        .findByUserId(userAdviceStatus.getUserId())
        .ifPresentOrElse(
            entity -> {
              entity.updateByDomain(userAdviceStatus);
              userAdviceStatusJpaRepository.save(entity);
            },
            () ->
                userAdviceStatusJpaRepository.save(UserAdviceStatusEntity.from(userAdviceStatus)));
  }
}
