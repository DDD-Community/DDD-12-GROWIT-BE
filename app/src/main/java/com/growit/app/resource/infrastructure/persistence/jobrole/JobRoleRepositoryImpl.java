package com.growit.app.resource.infrastructure.persistence.jobrole;

import com.growit.app.resource.domain.JobRole;
import com.growit.app.resource.domain.JobRoleRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class JobRoleRepositoryImpl implements JobRoleRepository {
  private final Map<String, JobRole> jobRoleMap = Stream.of(
    JobRole.from("기획자"),
    JobRole.from("디자이너"),
    JobRole.from("개발자")
  ).collect(Collectors.toMap(
    JobRole::getId,
    Function.identity()
  ));

  @Override
  public Optional<JobRole> findById(String id) {
    return Optional.ofNullable(jobRoleMap.get(id));
  }

  @Override
  public List<JobRole> findAll() {
    return new ArrayList<>(jobRoleMap.values());
  }
}
