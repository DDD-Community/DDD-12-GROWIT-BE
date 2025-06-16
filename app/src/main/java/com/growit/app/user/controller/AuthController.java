package com.growit.app.user.controller;

import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.controller.mapper.UserRequestMapper;
import com.growit.app.user.usecase.SignUpUseCase;
import lombok.RequiredArgsConstructor;
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

  @PostMapping("/signup")
  public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
    signUpUseCase.execute(mapper.toCommand(request));

    return ResponseEntity.ok().build();
  }
}
