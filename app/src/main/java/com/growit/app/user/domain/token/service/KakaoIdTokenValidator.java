package com.growit.app.user.domain.token.service;

import com.growit.app.common.config.oauth.KakaoKeys;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KakaoIdTokenValidator {

  private final JWKSource<SecurityContext> jwkSource;
  private final String clientId;

  @org.springframework.beans.factory.annotation.Autowired
  public KakaoIdTokenValidator(@Value("${app.oauth.kakao.client-id}") String clientId) {
    this.clientId = clientId;
    try {
      this.jwkSource = new RemoteJWKSet<>(new URL("https://kauth.kakao.com/.well-known/jwks.json"));
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Invalid Kakao JWKS URL", e);
    }
  }

  KakaoIdTokenValidator(String clientId, JWKSource<SecurityContext> jwkSource) {
    this.clientId = clientId;
    this.jwkSource = jwkSource;
  }

  public Map<String, Object> parseAndVerifyIdToken(String idToken, String nonce) {
    try {
      ConfigurableJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
      processor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource));

      JWTClaimsSet claims = processor.process(idToken, null);

      if (!KakaoKeys.ISSUER.equals(claims.getIssuer())) {
        throw new IllegalStateException("Invalid Kakao ID Token issuer");
      }

      if (!claims.getAudience().contains(clientId)) {
        throw new IllegalStateException("Invalid Kakao ID Token audience");
      }

      String tokenNonce = (String) claims.getClaim(KakaoKeys.NONCE);
      if (tokenNonce == null || !tokenNonce.equals(nonce)) {
        throw new IllegalStateException("Invalid Kakao ID Token nonce");
      }

      return claims.getClaims();
    } catch (ParseException | BadJWTException e) {
      log.error("Failed to parse or expired Kakao id_token", e);
      throw new IllegalArgumentException("유효하지 않은 형식이거나 만료된 카카오 id_token입니다.");
    } catch (IllegalArgumentException | IllegalStateException e) {
      log.error("카카오 id_token 클레임 검증 실패: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Failed to verify Kakao id_token signature via JWKS", e);
      throw new IllegalStateException("카카오 id_token 서명 검증에 실패했습니다.");
    }
  }
}
