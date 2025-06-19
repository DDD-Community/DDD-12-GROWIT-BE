package com.growit.app.user.controller;

import com.growit.app.user.domain.auth.dto.SignUpRequest;
import com.growit.app.user.usecase.SignUpUseCase;
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

  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@RequestBody SignUpRequest signUpRequest) {
    signUpUseCase.execute(signUpRequest);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

}
