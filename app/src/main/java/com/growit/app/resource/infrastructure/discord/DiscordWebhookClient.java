package com.growit.app.resource.infrastructure.discord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.resource.infrastructure.discord.dto.DiscordEmbed;
import com.growit.app.resource.infrastructure.discord.dto.DiscordField;
import com.growit.app.resource.infrastructure.discord.dto.DiscordWebhookRequest;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordWebhookClient {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  private static final String DISCORD_WEBHOOK_URL =
      "https://discord.com/api/webhooks/1416585254415765535/MAxIHEiDGl_zrGqbzaYjzXhymNrZdpPUJPIiQ0UArGM0_Fps4KqoufIcgqt3I_ikZmjd";

  public void sendInvitation(String phone) {
    try {
      String message = String.format("새로운 초대장 요청이 있습니다.%n전화번호: %s", phone);

      DiscordField phoneField = DiscordField.of("전화번호", phone, true);
      DiscordEmbed embed =
          DiscordEmbed.of(
              "📱 초대장 요청", "새로운 사용자가 초대장을 요청했습니다.", Instant.now().toString(), List.of(phoneField));

      DiscordWebhookRequest payload = DiscordWebhookRequest.of(message, List.of(embed));

      String json = objectMapper.writeValueAsString(payload);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<String> request = new HttpEntity<>(json, headers);
      ResponseEntity<String> response =
          restTemplate.postForEntity(DISCORD_WEBHOOK_URL, request, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        log.info("Discord webhook sent successfully for phone: {}", phone);
      } else {
        log.error(
            "Failed to send Discord webhook. Status: {}, Response: {}",
            response.getStatusCode(),
            response.getBody());
      }

    } catch (Exception e) {
      log.error("Error sending Discord webhook for phone: {}", phone, e);
    }
  }
}
