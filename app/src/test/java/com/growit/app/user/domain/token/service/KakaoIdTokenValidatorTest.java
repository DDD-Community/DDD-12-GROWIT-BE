package com.growit.app.user.domain.token.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.growit.app.common.config.oauth.KakaoKeys;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KakaoIdTokenValidatorTest {

  private static RSAKey rsaKey;
  private static JWKSource<SecurityContext> jwkSource;
  private KakaoIdTokenValidator validator;

  private final String clientId = "test-client-id";
  private final String validNonce = "test-nonce";

  @BeforeAll
  static void setUpAll() throws Exception {
    rsaKey = new RSAKeyGenerator(2048).keyID("test-kid").generate();
    jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
  }

  @BeforeEach
  void setUp() {
    validator = new KakaoIdTokenValidator(clientId, jwkSource);
  }

  private String generateToken(
      String issuer, String audience, String nonce, Date expirationTime, RSAKey signingKey)
      throws Exception {
    JWTClaimsSet.Builder builder =
        new JWTClaimsSet.Builder()
            .issuer(issuer)
            .audience(audience)
            .subject("test-sub")
            .expirationTime(expirationTime);

    if (nonce != null) {
      builder.claim(KakaoKeys.NONCE, nonce);
    }

    SignedJWT signedJWT =
        new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(signingKey.getKeyID()).build(),
            builder.build());

    signedJWT.sign(new RSASSASigner(signingKey));
    return signedJWT.serialize();
  }

  @Test
  @DisplayName("유효한 토큰은 정상적으로 검증된다")
  void validToken_Success() throws Exception {
    String token =
        generateToken(
            KakaoKeys.ISSUER,
            clientId,
            validNonce,
            Date.from(Instant.now().plus(1, ChronoUnit.HOURS)),
            rsaKey);

    Map<String, Object> claims = validator.parseAndVerifyIdToken(token, validNonce);

    assertThat(claims).isNotNull();
    assertThat(claims.get("sub")).isEqualTo("test-sub");
  }

  @Test
  @DisplayName("잘못된 발급자(issuer)면 예외가 발생한다")
  void invalidIssuer_ThrowsException() throws Exception {
    String token =
        generateToken(
            "https://invalid.issuer.com",
            clientId,
            validNonce,
            Date.from(Instant.now().plus(1, ChronoUnit.HOURS)),
            rsaKey);

    assertThatThrownBy(() -> validator.parseAndVerifyIdToken(token, validNonce))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Invalid Kakao ID Token issuer");
  }

  @Test
  @DisplayName("잘못된 대상자(audience)면 예외가 발생한다")
  void invalidAudience_ThrowsException() throws Exception {
    String token =
        generateToken(
            KakaoKeys.ISSUER,
            "wrong-client-id",
            validNonce,
            Date.from(Instant.now().plus(1, ChronoUnit.HOURS)),
            rsaKey);

    assertThatThrownBy(() -> validator.parseAndVerifyIdToken(token, validNonce))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Invalid Kakao ID Token audience");
  }

  @Test
  @DisplayName("잘못된 논스(nonce)면 예외가 발생한다")
  void invalidNonce_ThrowsException() throws Exception {
    String token =
        generateToken(
            KakaoKeys.ISSUER,
            clientId,
            "wrong-nonce",
            Date.from(Instant.now().plus(1, ChronoUnit.HOURS)),
            rsaKey);

    assertThatThrownBy(() -> validator.parseAndVerifyIdToken(token, validNonce))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Invalid Kakao ID Token nonce");
  }

  @Test
  @DisplayName("만료된 토큰이면 예외가 발생한다")
  void expiredToken_ThrowsException() throws Exception {
    String token =
        generateToken(
            KakaoKeys.ISSUER,
            clientId,
            validNonce,
            Date.from(Instant.now().minus(1, ChronoUnit.HOURS)),
            rsaKey);

    assertThatThrownBy(() -> validator.parseAndVerifyIdToken(token, validNonce))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("유효하지 않은 형식이거나 만료된 카카오 id_token입니다.");
  }

  @Test
  @DisplayName("잘못된 서명이면 예외가 발생한다")
  void invalidSignature_ThrowsException() throws Exception {
    RSAKey wrongKey = new RSAKeyGenerator(2048).keyID("wrong-kid").generate();
    String token =
        generateToken(
            KakaoKeys.ISSUER,
            clientId,
            validNonce,
            Date.from(Instant.now().plus(1, ChronoUnit.HOURS)),
            wrongKey);

    assertThatThrownBy(() -> validator.parseAndVerifyIdToken(token, validNonce))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("카카오 id_token 서명 검증에 실패했습니다.");
  }
}
