package com.growit.app.user.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.controller.dto.response.SayingResponse;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
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

  @GetMapping("/saying")
  public ResponseEntity<ApiResponse<SayingResponse>> getSaying() {
    return ResponseEntity.ok(
        ApiResponse.success(new SayingResponse("성공은 매일 반복되는 작은 노력들의 합이다냥!!", "그로냥")));
  }
}
