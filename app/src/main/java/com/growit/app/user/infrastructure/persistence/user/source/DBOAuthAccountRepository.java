package com.growit.app.user.infrastructure.persistence.user.source;

import com.growit.app.user.infrastructure.persistence.user.source.entity.OAuthAccountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBOAuthAccountRepository extends JpaRepository<OAuthAccountEntity, Long> {
  Optional<OAuthAccountEntity> findByProviderAndProviderId(String provider, String providerId);
  
  boolean existsByUserIdAndProvider(String userId, String provider);
  
  boolean existsByUserId(String userId);
}
