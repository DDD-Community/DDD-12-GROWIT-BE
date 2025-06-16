package com.growit.app.user.domain.repository;

import com.growit.app.user.domain.entity.JobRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRoleRepository extends JpaRepository<JobRoleEntity, UUID>, JobRoleQueryRepository {
}
