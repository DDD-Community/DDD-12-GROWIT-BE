package com.growit.app.resource.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.growit.app.resource.domain.jobrole.command.SyncJobRolesCommand;
import com.growit.app.resource.domain.jobrole.repository.JobRoleRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SyncJobRolesUseCaseTest {

  @Mock private JobRoleRepository jobRoleRepository;

  @InjectMocks private SyncJobRolesUseCase syncJobRolesUseCase;

  @Test
  void should_sync_job_roles_successfully() {
    // given
    List<SyncJobRolesCommand.JobRoleData> jobRoleData =
        Arrays.asList(
            new SyncJobRolesCommand.JobRoleData("dev", "개발자"),
            new SyncJobRolesCommand.JobRoleData("designer", "디자이너"));
    SyncJobRolesCommand command = new SyncJobRolesCommand(jobRoleData);

    // when
    syncJobRolesUseCase.execute(command);

    // then
    verify(jobRoleRepository).syncAll(any(List.class));
  }
}
