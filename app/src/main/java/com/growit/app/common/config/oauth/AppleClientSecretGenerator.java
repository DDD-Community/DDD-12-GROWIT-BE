package com.growit.app.common.config.oauth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleClientSecretGenerator {

  @Value("${app.oauth.apple.team-id}")
  private String teamId;

  @Value("${app.oauth.apple.client-id}")
  private String clientId;

  @Value("${app.oauth.apple.key-id}")
  private String keyId;

  @Value("${app.oauth.apple.private-key}")
  private String privateKey;

  public String createClientSecret() {
    Date expirationDate =
        Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());

    return Jwts.builder()
        .setHeaderParam("kid", keyId)
        .setHeaderParam("alg", "ES256")
        .setIssuer(teamId)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(expirationDate)
        .setAudience("https://appleid.apple.com")
        .setSubject(clientId)
        .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
        .compact();
  }

  private PrivateKey getPrivateKey() {
    try {
      String privateKeyContent =
          privateKey
              .replace("\\n", "")
              .replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s+", "");

      byte[] encoded = Base64.getDecoder().decode(privateKeyContent);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
      KeyFactory keyFactory = KeyFactory.getInstance("EC");
      return keyFactory.generatePrivate(keySpec);
    } catch (Exception e) {
      log.error("Failed to parse Apple private key", e);
      throw new IllegalStateException("Failed to parse Apple private key", e);
    }
  }
}
