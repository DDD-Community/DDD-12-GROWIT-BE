package com.growit.app.advice.controller;

import com.growit.app.advice.controller.dto.response.GrorongAdviceResponse;
import com.growit.app.advice.controller.dto.response.MentorAdviceResponse;
import com.growit.app.advice.controller.mapper.GrorongAdviceResponseMapper;
import com.growit.app.advice.controller.mapper.MentorAdviceResponseMapper;
import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.usecase.GetGrorongAdviceUseCase;
import com.growit.app.advice.usecase.GetMentorAdviceUseCase;
import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/advice")
@RequiredArgsConstructor
public class GrorongAdviceController {

  private final GetGrorongAdviceUseCase getGrorongAdviceUseCase;
  private final GetMentorAdviceUseCase getMentorAdviceUseCase;
  private final GrorongAdviceResponseMapper grorongAdviceResponseMapper;
  private final MentorAdviceResponseMapper mentorAdviceResponseMapper;

  @GetMapping("/grorong")
  public ResponseEntity<ApiResponse<GrorongAdviceResponse>> getGrorongAdvice(
      @AuthenticationPrincipal User user) {
    Grorong result = getGrorongAdviceUseCase.execute(user.getId());
    GrorongAdviceResponse response = grorongAdviceResponseMapper.toResponse(result);

    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/mentor")
  public ResponseEntity<ApiResponse<MentorAdviceResponse>> getMentorAdvice(
      @AuthenticationPrincipal User user) {
    MentorAdvice mentorAdvice = getMentorAdviceUseCase.execute(user);
    MentorAdviceResponse response =
        mentorAdvice == null ? null : mentorAdviceResponseMapper.toResponse(mentorAdvice);

    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
