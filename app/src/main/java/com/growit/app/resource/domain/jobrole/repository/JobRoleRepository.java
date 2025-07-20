package com.growit.app.resource.domain.jobrole.repository;

import com.growit.app.resource.domain.jobrole.JobRole;
import java.util.List;
import java.util.Optional;

public interface JobRoleRepository {
  List<JobRole> findAll();

  Optional<JobRole> findById(String id);

  void save(JobRole jobRole);
}
