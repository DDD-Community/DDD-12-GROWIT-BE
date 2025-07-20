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
  private final DBJobRoleRepository dbjobRoleRepository;
  private final JobRoleDBMapper jobRoleDBMapper;

  @Override
  public List<JobRole> findAll() {
    return dbjobRoleRepository.findAll().stream().map(jobRoleDBMapper::toDomain).toList();
  }

  @Override
  public Optional<JobRole> findById(String uId) {
    return dbjobRoleRepository.findByUid(uId).map(jobRoleDBMapper::toDomain);
  }

  @Override
  @Transactional
  public void save(JobRole jobRole) {
    JobRoleEntity entity = jobRoleDBMapper.toEntity(jobRole);
    dbjobRoleRepository.save(entity);
  }

  @Override
  @Transactional
  public void deleteAll() {
    dbjobRoleRepository.deleteAll();
  }

  @Override
  @Transactional
  public void syncAll(List<JobRole> jobRoles) {
    // Clear existing data
    dbjobRoleRepository.deleteAll();
    // Save new data
    List<JobRoleEntity> entities = jobRoles.stream().map(jobRoleDBMapper::toEntity).toList();
    dbjobRoleRepository.saveAll(entities);
  }
}
