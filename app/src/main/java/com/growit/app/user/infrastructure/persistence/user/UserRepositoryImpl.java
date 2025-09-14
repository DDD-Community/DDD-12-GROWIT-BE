package com.growit.app.user.infrastructure.persistence.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.infrastructure.persistence.user.source.DBOAuthAccountRepository;
import com.growit.app.user.infrastructure.persistence.user.source.DBUserRepository;
import com.growit.app.user.infrastructure.persistence.user.source.entity.OAuthAccountEntity;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
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
  private final DBOAuthAccountRepository dboAuthAccountRepository;

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
    Optional<UserEntity> userEntity = dbUserRepository.findByUid(user.getId());
    if (userEntity.isPresent()) {
      userEntity.get().updateByDomain(user);
      dbUserRepository.save(userEntity.get());
    } else {
      dbUserRepository.save(userDBMapper.toEntity(user));
    }
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    Page<UserEntity> page = dbUserRepository.findAll(pageable);
    List<User> content = page.getContent().stream().map(userDBMapper::toDomain).toList();
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  public Optional<User> findExistingUser(String provider, String providerId) {
    Optional<OAuthAccountEntity> oAuthAccount = dboAuthAccountRepository.findByProviderAndProviderId(provider, providerId);

    return oAuthAccount.flatMap(oAuthAccountEntity -> dbUserRepository.findByUid(oAuthAccountEntity.getUserId()).map(userDBMapper::toDomain));
  }

  @Override
  public boolean existsByUserIdAndProvider(String userId, String provider) {
    return dboAuthAccountRepository.existsByUserIdAndProvider(userId, provider);
  }

  @Override
  public boolean hasAnyOAuthAccount(String userId) {
    return dboAuthAccountRepository.existsByUserId(userId);
  }
}
