package com.growit.app.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @GetMapping("/")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Growit Server is running!");
  }
}
