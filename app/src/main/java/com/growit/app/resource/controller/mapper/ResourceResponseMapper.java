package com.growit.app.resource.controller.mapper;

import com.growit.app.resource.controller.dto.response.JobRoleResponse;
import com.growit.app.resource.controller.dto.response.SayingResponse;
import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.domain.saying.Saying;
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
}
