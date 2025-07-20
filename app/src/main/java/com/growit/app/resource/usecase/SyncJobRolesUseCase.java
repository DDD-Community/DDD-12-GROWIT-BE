package com.growit.app.resource.usecase;

import com.growit.app.resource.domain.jobrole.command.SyncJobRolesCommand;
import com.growit.app.resource.domain.jobrole.repository.JobRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SyncJobRolesUseCase {
  private final JobRoleRepository jobRoleRepository;

  @Transactional
  public void execute(SyncJobRolesCommand command) {
    command.jobRoles().forEach(jobRoleRepository::save);
  }
}
