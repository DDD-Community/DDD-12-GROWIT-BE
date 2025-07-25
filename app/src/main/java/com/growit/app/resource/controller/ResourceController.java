package com.growit.app.resource.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.resource.controller.dto.response.SayingResponse;
import com.growit.app.resource.controller.mapper.ResourceResponseMapper;
import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.domain.jobrole.repository.JobRoleRepository;
import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.usecase.GetSayingUseCase;
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
  private final GetSayingUseCase getSayingUseCase;
  private final ResourceResponseMapper responseMapper;

  @GetMapping("/jobroles")
  public ResponseEntity<ApiResponse<Map<String, Object>>> getAllJobRoles() {
    List<JobRole> jobRoles = jobRoleRepository.findAll();
    return ResponseEntity.ok(
        ApiResponse.success(Map.of("jobRoles", responseMapper.toJobRoleResponseList(jobRoles))));
  }

  @GetMapping("/saying")
  public ResponseEntity<ApiResponse<SayingResponse>> getSaying() {
    Saying saying = getSayingUseCase.execute();

    return ResponseEntity.ok(ApiResponse.success(responseMapper.toSayingResponse(saying)));
  }
}
