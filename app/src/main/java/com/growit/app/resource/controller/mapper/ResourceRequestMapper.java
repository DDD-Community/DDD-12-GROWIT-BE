package com.growit.app.resource.controller.mapper;

import com.growit.app.resource.controller.dto.request.SyncJobRolesRequest;
import com.growit.app.resource.controller.dto.request.SyncSayingsRequest;
import com.growit.app.resource.domain.jobrole.command.SyncJobRolesCommand;
import com.growit.app.resource.domain.saying.command.SyncSayingsCommand;
import org.springframework.stereotype.Component;

@Component
public class ResourceRequestMapper {

  public SyncJobRolesCommand toJobRolesCommand(SyncJobRolesRequest request) {
    return new SyncJobRolesCommand(request.getJobRoles());
  }

  public SyncSayingsCommand toSayingsCommand(SyncSayingsRequest request) {
    return new SyncSayingsCommand(request.getSayings());
  }
}
