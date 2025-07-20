package com.growit.app.resource.controller.mapper;

import com.growit.app.resource.controller.dto.request.SyncJobRolesRequest;
import com.growit.app.resource.controller.dto.request.SyncSayingsRequest;
import com.growit.app.resource.controller.dto.response.JobRoleResponse;
import com.growit.app.resource.controller.dto.response.SayingResponse;
import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.domain.jobrole.command.SyncJobRolesCommand;
import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.domain.saying.command.SyncSayingsCommand;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResourceResponseMapper {

  private static JobRoleResponse toJobRoleResponse(JobRole jr) {
    return new JobRoleResponse(jr.getId(), jr.getName());
  }

  public List<JobRoleResponse> toJobRoleResponseList(List<JobRole> jobRoles) {
    return jobRoles.stream().map(ResourceResponseMapper::toJobRoleResponse).toList();
  }

  public SayingResponse toSayingResponse(Saying saying) {
    return new SayingResponse(saying.getMessage(), saying.getAuthor());
  }

  public SyncJobRolesCommand toCommand(SyncJobRolesRequest request) {
    List<SyncJobRolesCommand.JobRoleData> jobRoleData =
        request.getJobRoles().stream()
            .map(item -> new SyncJobRolesCommand.JobRoleData(item.getId(), item.getName()))
            .toList();
    return new SyncJobRolesCommand(jobRoleData);
  }

  public SyncSayingsCommand toCommand(SyncSayingsRequest request) {
    List<SyncSayingsCommand.SayingData> sayingData =
        request.getSayings().stream()
            .map(
                item ->
                    new SyncSayingsCommand.SayingData(
                        item.getId(), item.getMessage(), item.getAuthor()))
            .toList();
    return new SyncSayingsCommand(sayingData);
  }
}
