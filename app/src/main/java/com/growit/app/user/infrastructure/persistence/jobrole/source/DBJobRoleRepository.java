package com.growit.app.user.infrastructure.persistence.jobrole.source;
import com.growit.app.user.infrastructure.persistence.jobrole.source.entity.JobRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DBJobRoleRepository extends JpaRepository<JobRoleEntity, String> {
  Optional<JobRoleEntity> findByUid(String uid);
}
