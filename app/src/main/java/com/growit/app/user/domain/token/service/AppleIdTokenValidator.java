package com.growit.app.user.domain.token.service;

import com.growit.app.common.config.oauth.AppleKeys;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppleIdTokenValidator {

    private static final String APPLE_ISSUER = "https://appleid.apple.com";
    private static final RemoteJWKSet<SecurityContext> APPLE_JWKS;

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
                throw new IllegalStateException("Invalid Apple ID Token issuer");
            }

            return claims.getClaims();
        } catch (ParseException e) {
            log.error("Failed to parse Apple id_token", e);
            throw new IllegalArgumentException("Invalid Apple id_token format");
        } catch (Exception e) {
            log.error("Failed to verify Apple id_token signature via JWKS", e);
            throw new IllegalStateException("Apple id_token verification failed");
        }
    }
}
