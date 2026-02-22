package com.growit.app.user.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.controller.dto.request.ReissueRequest;
import com.growit.app.user.controller.dto.request.SignInAppleRequest;
import com.growit.app.user.controller.dto.request.SignInRequest;
import com.growit.app.user.controller.dto.request.SignUpAppleRequest;
import com.growit.app.user.controller.dto.request.SignUpKaKaoRequest;
import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.controller.mapper.RequestMapper;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.dto.RequiredConsentCommand;
import com.growit.app.user.domain.user.dto.SignInAppleCommand;
import com.growit.app.user.domain.user.dto.SignUpAppleCommand;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.dto.SignUpKaKaoCommand;
import com.growit.app.user.usecase.ReissueUseCase;
import com.growit.app.user.usecase.SignInAppleResult;
import com.growit.app.user.usecase.SignInAppleUseCase;
import com.growit.app.user.usecase.SignInUseCase;
import com.growit.app.user.usecase.SignUpAppleUseCase;
import com.growit.app.user.usecase.SignUpKaKaoUseCase;
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
  private final SignInAppleUseCase signInAppleUseCase;
  private final SignUpKaKaoUseCase signUpKaKaoUseCase;
  private final SignUpAppleUseCase signUpAppleUseCase;
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

  @PostMapping("/signin")
  public ResponseEntity<ApiResponse<TokenResponse>> signin(
      @Valid @RequestBody SignInRequest signInRequest) {
    Token token = signInUseCase.execute(requestMapper.toSignInCommand(signInRequest));
    return ResponseEntity.ok(ApiResponse.success(responseMapper.toTokenResponse(token)));
  }

  @PostMapping("/signup/kakao")
  public ResponseEntity<Void> signupWithKaKao(
      @Valid @RequestBody SignUpKaKaoRequest signUpRequest) {
    SignUpKaKaoCommand signUpCommand = requestMapper.toSignUpKaKaoCommand(signUpRequest);
    RequiredConsentCommand requiredConsentCommand =
        requestMapper.toRequiredConsentCommand(signUpRequest.getRequiredConsent());
    signUpKaKaoUseCase.execute(signUpCommand, requiredConsentCommand);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/signup/apple")
  public ResponseEntity<Void> signupWithApple(
      @Valid @RequestBody SignUpAppleRequest signUpRequest) {
    SignUpAppleCommand signUpCommand = requestMapper.toSignUpAppleCommand(signUpRequest);
    RequiredConsentCommand requiredConsentCommand =
        requestMapper.toRequiredConsentCommand(signUpRequest.getRequiredConsent());
    signUpAppleUseCase.execute(signUpCommand, requiredConsentCommand);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/signin/apple")
  public ResponseEntity<ApiResponse<?>> signinWithApple(
      @Valid @RequestBody SignInAppleRequest signInAppleRequest) {
    SignInAppleCommand command =
        new SignInAppleCommand(
            signInAppleRequest.getIdToken(), signInAppleRequest.getAuthorizationCode());
    SignInAppleResult result = signInAppleUseCase.execute(command);

    if (result.isPendingSignup()) {
      return ResponseEntity.ok(ApiResponse.success(result.oauthResponse()));
    } else {
      return ResponseEntity.ok(ApiResponse.success(result.tokenResponse()));
    }
  }

  @GetMapping("/signin/kakao")
  public RedirectView signInWithKakao(
      @RequestParam(required = false, name = "redirect-uri") String redirectUri) {
    String oauth2Url = "/oauth2/authorization/kakao";
    if (redirectUri != null && !redirectUri.isBlank()) {
      oauth2Url += "?redirect-uri=" + redirectUri;
    }
    return new RedirectView(oauth2Url);
  }

  @PostMapping("/reissue")
  public ResponseEntity<ApiResponse<TokenResponse>> reissue(
      @Valid @RequestBody ReissueRequest reissueRequest) {
    Token token = reissueUseCase.execute(requestMapper.toReIssueCommand(reissueRequest));
    return ResponseEntity.ok(ApiResponse.success(responseMapper.toTokenResponse(token)));
  }
}
