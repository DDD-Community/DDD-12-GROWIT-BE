package com.growit.app.resource.infrastructure.persistence.jobrole.source;

import com.growit.app.resource.infrastructure.persistence.jobrole.source.entity.JobRoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBJobRoleRepository extends JpaRepository<JobRoleEntity, String> {
  Optional<JobRoleEntity> findByUid(String uid);
}
