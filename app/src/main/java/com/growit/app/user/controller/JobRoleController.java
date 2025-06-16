package com.growit.app.user.controller;

import com.growit.app.common.dto.Response;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class JobRoleController {

  private final JobRoleRepository jobRoleRepository;

  @GetMapping("/jobroles")
  public ResponseEntity<Response<List<JobRole>>> getAllJobRoles() {
    return ResponseEntity.ok(Response.ok(jobRoleRepository.findAll()));
  }
}
