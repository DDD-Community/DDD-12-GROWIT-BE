package com.growit.app.user.controller.resource.controller;

import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller("/resource")
@AllArgsConstructor
public class ResourceController {

  private final JobRoleRepository jobRoleRepository;

  @GetMapping("/jobroles")
  public ResponseEntity<List<JobRole>> getJobList() {
    return ResponseEntity.ok(jobRoleRepository.findAll());
  }
}
