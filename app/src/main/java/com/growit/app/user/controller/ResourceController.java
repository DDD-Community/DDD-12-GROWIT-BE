package com.growit.app.user.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.controller.dto.response.JobRoleResponse;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

  private final JobRoleRepository jobRoleRepository;

  @GetMapping("/jobroles")
  public ResponseEntity<ApiResponse<Map<String, Object>>> getAllJobRoles() {
    List<JobRole> jobRoles = jobRoleRepository.findAll();

    List<JobRoleResponse> responses = jobRoles.stream()
      .map(jr -> new JobRoleResponse(jr.getUid(), jr.getName()))
      .toList();

    return ResponseEntity.ok(ApiResponse.success(Map.of("jobRoles", responses)));
  }
}
