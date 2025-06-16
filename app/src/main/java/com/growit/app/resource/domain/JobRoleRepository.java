package com.growit.app.resource.domain;

import java.util.List;
import java.util.Optional;

public interface JobRoleRepository {
  Optional<JobRole> findById(String id);

  List<JobRole> findAll();
}
