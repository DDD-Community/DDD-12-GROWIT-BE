package com.growit.app.resource.usecase;

import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.domain.jobrole.command.SyncJobRolesCommand;
import com.growit.app.resource.domain.jobrole.repository.JobRoleRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SyncJobRolesUseCase {
  private final JobRoleRepository jobRoleRepository;

  @Transactional
  public void execute(SyncJobRolesCommand command) {
    List<JobRole> jobRoles =
        command.getJobRoles().stream()
            .map(data -> JobRole.builder().id(data.getId()).name(data.getName()).build())
            .toList();

    jobRoleRepository.syncAll(jobRoles);
  }
}
