package com.growit.app.resource.controller.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class SyncJobRolesRequest {
  private List<JobRoleItem> jobRoles;

  @Getter
  public static class JobRoleItem {
    private String id;
    private String name;
  }
}