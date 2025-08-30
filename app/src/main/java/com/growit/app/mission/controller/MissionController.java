package com.growit.app.mission.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.usecase.GetMissionUseCase;
import com.growit.app.user.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mission")
@RequiredArgsConstructor
public class MissionController {
  private final GetMissionUseCase getMissionUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<Mission>>> getMission(@AuthenticationPrincipal User user) {
    List<Mission> mission = getMissionUseCase.execute(user.getId());
    return ResponseEntity.ok(ApiResponse.success(mission));
  }
}
