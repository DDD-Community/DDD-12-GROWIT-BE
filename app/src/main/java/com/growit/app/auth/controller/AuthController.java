package com.growit.app.auth.controller;

import com.growit.app.auth.controller.dto.SignUpRequest;
import com.growit.app.auth.usecase.SignUpUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final SignUpUseCase signUpUseCase;

  public AuthController(SignUpUseCase signUpUseCase) {
    this.signUpUseCase = signUpUseCase;
  }

  @PostMapping("/signup")
  public ResponseEntity<SimpleResponse> signup(@RequestBody SignUpRequest request) {
    signUpUseCase.execute(request);
    return ResponseEntity
      .status(202)
      .body(new SimpleResponse(202, "가입 성공"));
  }


  public record SimpleResponse(int status, String message) {
  }
}
