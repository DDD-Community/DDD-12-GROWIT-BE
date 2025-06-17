package com.growit.app.auth.controller;

import com.growit.app.auth.controller.dto.SignUpRequest;
import com.growit.app.auth.usecase.SignUpUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  public record SimpleResponse(int status, String message) {
  }

  private final SignUpUseCase signUpUseCase;

  @PostMapping("/signup")
  public ResponseEntity<SimpleResponse> signup(@RequestBody SignUpRequest request) {
    signUpUseCase.execute(request);
    return ResponseEntity
      .status(202)
      .body(new SimpleResponse(202, "가입 성공"));
  }


}
