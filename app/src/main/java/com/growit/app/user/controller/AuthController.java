package com.growit.app.user.controller;

import com.growit.app.user.controller.dto.request.ReissueRequest;
import com.growit.app.user.controller.dto.request.SignInRequest;
import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.controller.mapper.UserRequestMapper;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.usecase.ReissueUseCase;
import com.growit.app.user.usecase.SignInUseCase;
import com.growit.app.user.usecase.SignUpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  private final UserRequestMapper mapper;
  private final SignUpUseCase signUpUseCase;
  private final SignInUseCase signInUseCase;
  private final ReissueUseCase reissueUseCase;

  @PostMapping("/signup")
  public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
    signUpUseCase.execute(mapper.toCommand(request));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/signin")
  public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequest request) {
    Token token = signInUseCase.execute(new Email(request.getEmail()), request.getPassword());
    return ResponseEntity.ok(mapper.toResponse(token));
  }

  @PostMapping("/reissue")
  public ResponseEntity<TokenResponse> reissue(@RequestBody ReissueRequest request) {
    Token token = reissueUseCase.execute(request.getRefreshToken());
    return ResponseEntity.ok(mapper.toResponse(token));
  }
}
