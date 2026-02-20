package com.growit.app.advice.controller;

import com.growit.app.advice.controller.dto.request.SajuFortuneRequest;
import com.growit.app.advice.usecase.GetSajuFortuneUseCase;
import com.growit.app.common.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/advice/saju")
@RequiredArgsConstructor
public class SajuController {

  private final GetSajuFortuneUseCase getSajuFortuneUseCase;

  @PostMapping("/fortune")
  public ResponseEntity<ApiResponse<Map<String, Object>>> getFortune(
      @Valid @RequestBody SajuFortuneRequest request) {
    Map<String, Object> result =
        getSajuFortuneUseCase.execute(
            request.getBirthDate(), request.getBirthTime(), request.getGender());
    return ResponseEntity.ok(ApiResponse.success(result));
  }
}
