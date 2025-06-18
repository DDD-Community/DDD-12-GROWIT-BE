package com.growit.app.user.domain.jobrole.repository;

import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.infrastructure.service.persistence.jobrole.source.entity.JobRoleEntity;

import java.util.List;
import java.util.Optional;

public interface JobRoleRepository {
  List<JobRole> findAll();
  Optional<JobRole> findById(String id);
}
