package com.growit.app.resource.infrastructure.persistence.jobrole;

import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.infrastructure.persistence.jobrole.source.entity.JobRoleEntity;
import org.springframework.stereotype.Component;

@Component
public class JobRoleDBMapper {

  public JobRole toDomain(JobRoleEntity jobRoleEntity) {
    if (jobRoleEntity == null) return null;
    return JobRole.builder()
        .id(jobRoleEntity.getUid())
        .name(jobRoleEntity.getName())
        .build();
  }

  public JobRoleEntity toEntity(JobRole jobRole) {
    if (jobRole == null) return null;
    return new JobRoleEntity(jobRole.getId(), jobRole.getName());
  }
}
