package com.growit.app.user.infrastructure.client;

import com.growit.app.common.config.oauth.AppleClientSecretGenerator;
import com.growit.app.user.domain.user.AppleTokenRevocationPort;
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
public class AppleTokenRevocationClient implements AppleTokenRevocationPort {

  private final AppleClientSecretGenerator appleClientSecretGenerator;
  private final RestTemplateBuilder restTemplateBuilder;

  @Value("${app.oauth.apple.client-id}")
  private String clientId;

  private static final String APPLE_REVOKE_URL = "https://appleid.apple.com/auth/revoke";

  private RestTemplate restTemplate;

  @PostConstruct
  public void init() {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Override
  public void revoke(String refreshToken) {
    try {
      String clientSecret = appleClientSecretGenerator.createClientSecret();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add("client_id", clientId);
      body.add("client_secret", clientSecret);
      body.add("token", refreshToken);
      body.add("token_type_hint", "refresh_token");

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

      ResponseEntity<String> response =
          restTemplate.exchange(APPLE_REVOKE_URL, HttpMethod.POST, request, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        log.info("Apple OAuth Revocation Successful");
      } else {
        log.error("Apple OAuth Revocation Failed: {}", response.getBody());
      }
    } catch (Exception e) {
      log.error("Apple OAuth Revocation Error", e);
    }
  }
}
