package com.growit.app.resource.infrastructure.persistence.saying.source;

import com.growit.app.resource.infrastructure.persistence.saying.source.entity.SayingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBSayingRepository extends JpaRepository<SayingEntity, String> {}
