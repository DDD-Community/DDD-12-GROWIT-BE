package com.growit.app.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestErrorLogController {
  private static final Logger logger = LoggerFactory.getLogger(TestErrorLogController.class);

  @GetMapping("/test-error-log")
  public String testErrorLog() {
    logger.error("error: test for cloudwatch metric filter");
    return "Error log sent!";
  }
}
