package com.growit.app.user.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.controller.dto.request.ReissueRequest;
import com.growit.app.user.controller.dto.request.SignInRequest;
import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.controller.mapper.RequestMapper;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.dto.RequiredConsentCommand;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.usecase.ReissueUseCase;
import com.growit.app.user.usecase.SignInUseCase;
import com.growit.app.user.usecase.SignUpUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final SignUpUseCase signUpUseCase;
  private final SignInUseCase signInUseCase;
  private final ReissueUseCase reissueUseCase;
  private final RequestMapper requestMapper;
  private final ResponseMapper responseMapper;

  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
    SignUpCommand signUpCommand = requestMapper.toSignUpCommand(signUpRequest);
    RequiredConsentCommand requiredConsentCommand =
        requestMapper.toRequiredConsentCommand(signUpRequest.getRequiredConsent());
    signUpUseCase.execute(signUpCommand, requiredConsentCommand);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/signin/kakao")
  public RedirectView loginWithKakao() {
    return new RedirectView("/oauth2/authorization/kakao");
  }

  @PostMapping("/signin")
  public ResponseEntity<ApiResponse<TokenResponse>> signin(
      @Valid @RequestBody SignInRequest signInRequest) {
    Token token = signInUseCase.execute(requestMapper.toSignInCommand(signInRequest));
    return ResponseEntity.ok(ApiResponse.success(responseMapper.toTokenResponse(token)));
  }

  @PostMapping("/reissue")
  public ResponseEntity<ApiResponse<TokenResponse>> reissue(
      @Valid @RequestBody ReissueRequest reissueRequest) {
    Token token = reissueUseCase.execute(requestMapper.toReIssueCommand(reissueRequest));
    return ResponseEntity.ok(ApiResponse.success(responseMapper.toTokenResponse(token)));
  }
}
