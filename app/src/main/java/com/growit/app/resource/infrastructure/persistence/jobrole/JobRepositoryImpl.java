package com.growit.app.resource.infrastructure.persistence.jobrole;

import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.domain.jobrole.repository.JobRoleRepository;
import com.growit.app.resource.infrastructure.persistence.jobrole.source.DBJobRoleRepository;
import com.growit.app.resource.infrastructure.persistence.jobrole.source.entity.JobRoleEntity;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JobRepositoryImpl implements JobRoleRepository {
  private final DBJobRoleRepository dbJobRoleRepository;
  private final JobRoleDBMapper jobRoleDBMapper;

  @Override
  public List<JobRole> findAll() {
    return dbJobRoleRepository.findAll().stream().map(jobRoleDBMapper::toDomain).toList();
  }

  @Override
  public Optional<JobRole> findById(String uId) {
    return dbJobRoleRepository.findByUid(uId).map(jobRoleDBMapper::toDomain);
  }

  @Override
  public void save(JobRole jobRole) {
    Optional<JobRoleEntity> existing = dbJobRoleRepository.findByUid(jobRole.getId());
    JobRoleEntity entity;
    if (existing.isPresent()) {
      entity = existing.get();
      entity.updateByDomain(jobRole);
    } else {
      entity = jobRoleDBMapper.toEntity(jobRole);
    }
    dbJobRoleRepository.save(entity);
  }
}
