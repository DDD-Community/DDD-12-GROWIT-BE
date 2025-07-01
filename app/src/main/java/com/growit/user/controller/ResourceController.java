package com.growit.user.controller;

import com.growit.common.response.ApiResponse;
import com.growit.user.controller.mapper.ResponseMapper;
import com.growit.user.domain.jobrole.JobRole;
import com.growit.user.domain.jobrole.repository.JobRoleRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {
  private final JobRoleRepository jobRoleRepository;
  private final ResponseMapper responseMapper;

  @GetMapping("/jobroles")
  public ResponseEntity<ApiResponse<Map<String, Object>>> getAllJobRoles() {
    List<JobRole> jobRoles = jobRoleRepository.findAll();
    return ResponseEntity.ok(
        ApiResponse.success(Map.of("jobRoles", responseMapper.toJobRoleResponseList(jobRoles))));
  }
}
