package com.growit.app.user.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.controller.dto.request.ReissueRequest;
import com.growit.app.user.controller.dto.request.SignInRequest;
import com.growit.app.user.controller.dto.request.TokenResponse;
import com.growit.app.user.domain.auth.dto.SignUpRequest;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.usecase.ReissueUseCase;
import com.growit.app.user.usecase.SignInUseCase;
import com.growit.app.user.usecase.SignUpUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final SignUpUseCase signUpUseCase;
  private final SignInUseCase signInUseCase;
  private final ReissueUseCase reissueUseCase;

  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
    signUpUseCase.execute(signUpRequest);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @PostMapping("/signin")
  public ResponseEntity<ApiResponse<TokenResponse>> signin(@RequestBody SignInRequest signInRequest) {
    Token token = signInUseCase.execute(signInRequest);
    TokenResponse response = TokenResponse.builder()
      .accessToken(token.accessToken())
      .refreshToken(token.refreshToken())
      .build();
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/reissue")
  public ResponseEntity<ApiResponse<TokenResponse>> reissue(@RequestBody ReissueRequest request) {
    Token token = reissueUseCase.execute(request.getRefreshToken());
    TokenResponse response = TokenResponse.builder()
      .accessToken(token.accessToken())
      .refreshToken(token.refreshToken())
      .build();
    return ResponseEntity.ok(ApiResponse.success(response));
  }

}
