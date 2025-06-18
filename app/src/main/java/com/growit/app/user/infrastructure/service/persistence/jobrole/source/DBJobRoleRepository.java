package com.growit.app.user.infrastructure.service.persistence.jobrole.source;

import com.growit.app.user.infrastructure.service.persistence.jobrole.source.entity.JobRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DBJobRoleRepository extends JpaRepository<JobRoleEntity, UUID>, JobRoleQueryRepository {
}
