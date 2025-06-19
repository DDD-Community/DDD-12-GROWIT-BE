package com.growit.app.user.infrastructure.persistence.jobrole;

import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
import com.growit.app.user.infrastructure.persistence.jobrole.source.DBJobRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
  public Optional<JobRole> findById(String id) {
    return dbjobRoleRepository.findById(id).map(jobRoleDBMapper::toDomain);
  }
}
