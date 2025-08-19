package com.growit.app.mission.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.mission.controller.dto.CreateMissionRequest;
import com.growit.app.mission.controller.mapper.MissionRequestMapper;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import com.growit.app.mission.usecase.CreateMissionUseCase;
import com.growit.app.mission.usecase.GetMissionUseCase;
import com.growit.app.mission.usecase.UpdateMissionUseCase;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mission")
@RequiredArgsConstructor
public class MissionController {
  private final CreateMissionUseCase createMissionUseCase;
  private final GetMissionUseCase getMissionUseCase;
  private final UpdateMissionUseCase updateMissionUseCase;
  private final MissionRequestMapper missionRequestMapper;
  private final MessageService messageService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<Mission>>> getMission(@AuthenticationPrincipal User user) {
    List<Mission> mission = getMissionUseCase.execute(user.getId());
    return ResponseEntity.ok(ApiResponse.success(mission));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<String>> createMission(
      @Valid @RequestBody CreateMissionRequest request) {
    CreateMissionCommand command = missionRequestMapper.toCommand(request);
    createMissionUseCase.execute(command);
    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.mission.created")));
  }

  @PutMapping("{id}")
  public ResponseEntity<ApiResponse<String>> finishedMission(
      @AuthenticationPrincipal User user, @PathVariable String id) {
    updateMissionUseCase.execute(id, user.getId());
    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.mission.finished")));
  }
}
