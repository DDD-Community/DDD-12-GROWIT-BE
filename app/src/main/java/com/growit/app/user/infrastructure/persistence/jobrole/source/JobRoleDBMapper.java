package com.growit.app.user.infrastructure.persistence.jobrole.source;

import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.infrastructure.persistence.jobrole.source.entity.JobRoleEntity;
import org.springframework.stereotype.Component;

@Component
public class JobRoleDBMapper {

  public JobRole toDomain(JobRoleEntity jobRoleEntity) {
    if (jobRoleEntity == null) return null;
    return JobRole.builder().id(String.valueOf(jobRoleEntity.getId())).name(jobRoleEntity.getName()).build();
  }

  public JobRoleEntity toEntity(JobRole jobRole) {
    if (jobRole == null) return null;
    return JobRoleEntity.builder().id(jobRole.getId()).name(jobRole.getName()).build();
  }

}
