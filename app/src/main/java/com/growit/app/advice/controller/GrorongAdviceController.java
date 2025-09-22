package com.growit.app.advice.controller;

import com.growit.app.advice.controller.dto.response.GrorongAdviceResponse;
import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.usecase.GetGrorongAdviceUseCase;
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

    @GetMapping("/grorong")
    public ResponseEntity<ApiResponse<GrorongAdviceResponse>> getGrorongAdvice(
            @AuthenticationPrincipal User user) {
        Grorong result = getGrorongAdviceUseCase.execute(user.getId());
        // mapper 추가
        GrorongAdviceResponse response = new GrorongAdviceResponse(result);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
