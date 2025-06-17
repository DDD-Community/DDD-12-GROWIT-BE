package com.growit.app.user.controller;

import com.growit.app.common.dto.Response;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  @GetMapping("/myprofile")
  public ResponseEntity<Response<User>> getUser(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(Response.ok(user));
  }
}
