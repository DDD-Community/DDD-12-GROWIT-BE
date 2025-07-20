package com.growit.app.resource.domain.jobrole.command;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SyncJobRolesCommand {
  private final List<JobRoleData> jobRoles;

  @Getter
  @AllArgsConstructor
  public static class JobRoleData {
    private final String id;
    private final String name;
  }
}