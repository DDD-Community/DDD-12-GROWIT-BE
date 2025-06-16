package com.growit.app.user.domain.repository;

import com.growit.app.user.domain.entity.JobRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobRoleRepository extends JpaRepository<JobRoleEntity, UUID> {
  // 이름으로 조회하는 메서드 추가
  Optional<JobRoleEntity> findByName(String name);
}
