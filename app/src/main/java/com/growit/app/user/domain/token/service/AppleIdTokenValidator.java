package com.growit.app.user.domain.token.service;

import com.nimbusds.jose.JWSAlgorithm;
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
public class AppleIdTokenValidator {

  private static final String APPLE_ISSUER = "https://appleid.apple.com";
  private static final RemoteJWKSet<SecurityContext> APPLE_JWKS;

  @Value("${app.oauth.apple.client-id}")
  private String clientId;

  static {
    try {
      APPLE_JWKS = new RemoteJWKSet<>(new URL("https://appleid.apple.com/auth/keys"));
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Invalid Apple JWKS URL", e);
    }
  }

  public Map<String, Object> parseAndVerifyIdToken(String idToken) {
    try {
      ConfigurableJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
      processor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, APPLE_JWKS));

      JWTClaimsSet claims = processor.process(idToken, null);

      if (!APPLE_ISSUER.equals(claims.getIssuer())) {
        log.error(
            "Apple id_token issuer 불일치: expected={}, actual={}", APPLE_ISSUER, claims.getIssuer());
        throw new IllegalArgumentException(
            "Apple id_token 발급자(issuer)가 유효하지 않습니다. Apple에서 발급된 토큰인지 확인해 주세요.");
      }

      if (!claims.getAudience().contains(clientId)) {
        log.error(
            "Apple id_token audience 불일치: expectedClientId={}, actual={}",
            clientId,
            claims.getAudience());
        throw new IllegalArgumentException(
            "Apple id_token의 대상(audience)이 현재 앱 클라이언트 ID와 일치하지 않습니다.");
      }

      return claims.getClaims();
    } catch (ParseException e) {
      log.error("Apple id_token 파싱 실패: {}", e.getMessage());
      throw new IllegalArgumentException("Apple id_token 형식이 올바르지 않습니다. 유효한 토큰을 전달해 주세요.");
    } catch (BadJWTException e) {
      log.error("Apple id_token 유효성 검증 실패: {}", e.getMessage());
      throw new IllegalArgumentException("Apple id_token이 만료되었거나 유효하지 않습니다. 다시 로그인해 주세요.");
    } catch (IllegalStateException e) {
      log.error("Apple id_token 클레임 검증 실패: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error(
          "Apple id_token 서명 검증 중 예외 발생: exceptionType={}, message={}",
          e.getClass().getSimpleName(),
          e.getMessage(),
          e);
      throw new IllegalArgumentException("Apple id_token 서명 검증에 실패했습니다. 잠시 후 다시 시도해 주세요.", e);
    }
  }
}
