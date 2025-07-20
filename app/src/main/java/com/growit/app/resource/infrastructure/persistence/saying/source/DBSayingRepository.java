package com.growit.app.resource.infrastructure.persistence.saying.source;

import com.growit.app.resource.infrastructure.persistence.saying.source.entity.SayingEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBSayingRepository extends JpaRepository<SayingEntity, Long> {
  Optional<SayingEntity> findByUid(String uid);
}
