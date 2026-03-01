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
        log.error(
            "카카오 id_token issuer 불일치: expected={}, actual={}",
            KakaoKeys.ISSUER,
            claims.getIssuer());
        throw new IllegalArgumentException(
            "카카오 id_token 발급자(issuer)가 유효하지 않습니다. 카카오에서 발급된 토큰인지 확인해 주세요.");
      }

      if (!claims.getAudience().contains(clientId)) {
        log.error(
            "카카오 id_token audience 불일치: expectedClientId={}, actual={}",
            clientId,
            claims.getAudience());
        throw new IllegalArgumentException("카카오 id_token의 대상(audience)이 현재 앱 클라이언트 ID와 일치하지 않습니다.");
      }

      String tokenNonce = (String) claims.getClaim(KakaoKeys.NONCE);
      if (tokenNonce == null) {
        throw new IllegalArgumentException(
            "카카오 id_token에 nonce 클레임이 존재하지 않습니다. 로그인 요청을 다시 시도해 주세요.");
      }
      if (!tokenNonce.equals(nonce)) {
        throw new IllegalArgumentException(
            "카카오 id_token의 nonce가 요청 값과 일치하지 않습니다. 재로그인 후 다시 시도해 주세요.");
      }

      return claims.getClaims();
    } catch (ParseException | BadJWTException e) {
      log.error("카카오 id_token 파싱 또는 만료 오류: {}", e.getMessage());
      throw new IllegalArgumentException("유효하지 않은 형식이거나 만료된 카카오 id_token입니다.");
    } catch (IllegalArgumentException | IllegalStateException e) {
      log.error("카카오 id_token 클레임 검증 실패: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error(
          "카카오 id_token 서명 검증 중 예외 발생: exceptionType={}, message={}",
          e.getClass().getSimpleName(),
          e.getMessage(),
          e);
      throw new IllegalArgumentException("카카오 id_token 서명 검증에 실패했습니다. 잠시 후 다시 시도해 주세요.", e);
    }
  }
}
