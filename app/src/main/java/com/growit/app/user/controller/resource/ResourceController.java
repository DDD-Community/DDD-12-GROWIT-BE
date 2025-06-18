package com.growit.app.user.controller.resource;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/resource")
@RequiredArgsConstructor
public class ResourceController {

  private final JobRoleRepository jobRoleRepository;

  @GetMapping("jobroles")
  public ResponseEntity<ApiResponse<List<JobRole>>> jobRoles() {
    List<JobRole> jobRoles = jobRoleRepository.findAll();
    ApiResponse<List<JobRole>> response = ApiResponse.success(jobRoles);
    return ResponseEntity.ok(response);
  }
}
