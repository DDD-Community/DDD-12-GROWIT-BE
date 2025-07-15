package com.growit.app.resource.infrastructure.persistence.jobrole;

import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.infrastructure.persistence.jobrole.source.entity.JobRoleEntity;
import org.springframework.stereotype.Component;

@Component
public class JobRoleDBMapper {

  public JobRole toDomain(JobRoleEntity jobRoleEntity) {
    if (jobRoleEntity == null) return null;
    return JobRole.builder()
        .id(String.valueOf(jobRoleEntity.getUid()))
        .name(jobRoleEntity.getName())
        .build();
  }
}
