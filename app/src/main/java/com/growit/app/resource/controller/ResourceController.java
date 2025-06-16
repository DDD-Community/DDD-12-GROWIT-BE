package com.growit.app.resource.controller;

import com.growit.app.common.dto.Response;
import com.growit.app.resource.domain.JobRole;
import com.growit.app.resource.domain.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class ResourceController {

  private final JobRoleRepository jobRoleRepository;

  @GetMapping("/jobroles")
  public ResponseEntity<Response<List<JobRole>>> getAllJobRoles() {
    return ResponseEntity.ok(Response.ok(jobRoleRepository.findAll()));
  }
}
