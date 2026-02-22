package com.growit.app.user.infrastructure.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.config.oauth.AppleClientSecretGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleAuthClient {

  private final AppleClientSecretGenerator appleClientSecretGenerator;
  private final RestTemplateBuilder restTemplateBuilder;
  private final ObjectMapper objectMapper;

  @Value("${app.oauth.apple.client-id}")
  private String clientId;

  private static final String APPLE_TOKEN_URL = "https://appleid.apple.com/auth/token";

  private RestTemplate restTemplate;

  @PostConstruct
  public void init() {
    this.restTemplate = restTemplateBuilder.build();
  }

  public String getRefreshToken(String authorizationCode) {
    try {
      String clientSecret = appleClientSecretGenerator.createClientSecret();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add("client_id", clientId);
      body.add("client_secret", clientSecret);
      body.add("code", authorizationCode);
      body.add("grant_type", "authorization_code");

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

      ResponseEntity<String> response =
          restTemplate.exchange(APPLE_TOKEN_URL, HttpMethod.POST, request, String.class);

      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        AppleTokenResponse tokenResponse =
            objectMapper.readValue(response.getBody(), AppleTokenResponse.class);
        log.info("Successfully fetched refresh token from Apple");
        return tokenResponse.getRefreshToken();
      } else {
        log.error("Failed to fetch refresh token from Apple: {}", response.getBody());
        throw new IllegalStateException("Failed to exchange authorization code with Apple");
      }
    } catch (Exception e) {
      log.error("Error exchanging authorization code with Apple", e);
      throw new IllegalStateException("Error communicating with Apple Token API", e);
    }
  }
}
