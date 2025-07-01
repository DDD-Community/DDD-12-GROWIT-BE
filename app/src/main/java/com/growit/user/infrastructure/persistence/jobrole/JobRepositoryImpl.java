package com.growit.user.infrastructure.persistence.jobrole;

import com.growit.user.domain.jobrole.JobRole;
import com.growit.user.domain.jobrole.repository.JobRoleRepository;
import com.growit.user.infrastructure.persistence.jobrole.source.DBJobRoleRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
