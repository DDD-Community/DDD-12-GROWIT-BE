package com.growit.app.common.exception;

import com.growit.app.common.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestErrorLogController {
  private static final Logger logger = LoggerFactory.getLogger(TestErrorLogController.class);

  private final NotificationService notificationService;

  public TestErrorLogController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping("/test/discord-message")
  public String testDiscordMessage() {
    logger.error("discord: test message for discord webhook");
    notificationService.sendErrorNotification(
        "/test-discord-message", "GET", "TEST_ERROR", "Discord webhook test message");
    return "Discord message sent!";
  }
}
