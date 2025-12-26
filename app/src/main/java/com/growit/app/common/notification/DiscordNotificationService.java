package com.growit.app.common.notification;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DiscordNotificationService implements NotificationService {

  private final RestTemplate restTemplate = new RestTemplate();

  private static final String DISCORD_WEBHOOK_URL =
      "https://discord.com/api/webhooks/1416585254415765535/MAxIHEiDGl_zrGqbzaYjzXhymNrZdpPUJPIiQ0UArGM0_Fps4KqoufIcgqt3I_ikZmjd";

  @Override
  public void sendErrorNotification(String uri, String method, String errorType, String message) {
    try {
      Map<String, Object> discordMessage = new HashMap<>();
      discordMessage.put(
          "content",
          String.format(
              """
          🚨 **500 Internal Server Error** 🚨
          **URI:** %s
          **Method:** %s
          **Error:** %s
          **Message:** %s
          """,
              uri, method, errorType, message));

      restTemplate.postForEntity(DISCORD_WEBHOOK_URL, discordMessage, String.class);
    } catch (Exception e) {
      log.error("Failed to send Discord notification", e);
    }
  }
}
