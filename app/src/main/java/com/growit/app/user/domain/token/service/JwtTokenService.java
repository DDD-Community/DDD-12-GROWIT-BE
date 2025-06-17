package com.growit.app.user.domain.token.service;

import com.growit.app.common.config.jwt.JwtProperties;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class JwtTokenService implements TokenService {
  private final JwtProperties jwtProperties;

  public JwtTokenService(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parser()

        .setSigningKey(jwtProperties.getSecretKey())
        .parseClaimsJws(token)
        .getBody();
    } catch (ExpiredJwtException | IllegalArgumentException | MalformedJwtException | UnsupportedJwtException |
             SignatureException e) {
      throw new InvalidJwtTokenError();
    }
  }

  private Claims createClaim(String id) {
    final Claims claims = Jwts.claims();
    claims.put("id", id);

    return claims;
  }

  private String createToken(Claims claims, int second) {
    final Date now = new Date();
    final Date expiredDate = new Date(now.getTime() + (second * 1000L));

    return Jwts.builder()
      .setClaims(claims)
      .setExpiration(expiredDate)
      .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
      .compact();
  }

  private boolean isExpiredSoon(Date expirationDate) {
    final Date currentDate = new Date();

    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(expirationDate);
    calendar.add(Calendar.DATE, -30);
    final Date thirtyDaysBefore = calendar.getTime();

    return currentDate.after(thirtyDaysBefore);
  }

  public String getId(String token) {
    final Claims claims = parseClaims(token);

    return claims.get("id", String.class);
  }

  @Override
  public boolean isValid(String token) {
    final Claims claims = parseClaims(token);
    return claims.getExpiration().after(new Date());
  }

  @Override
  public Token createToken(User user) {
    final Claims claims = createClaim(user.getId());
    final String accessToken = createToken(claims, jwtProperties.getExpiredSecond());
    final String refreshToken = createToken(claims, jwtProperties.getRefreshExpiredSecond());

    return new Token(accessToken, refreshToken);
  }

  @Override
  public Token reIssue(String token) {
    final Claims claims = parseClaims(token);
    final Date expirationDate = claims.getExpiration();

    final String accessToken = createToken(claims, jwtProperties.getExpiredSecond());
    if (isExpiredSoon(expirationDate)) {
      final String newRefreshToken = createToken(claims, jwtProperties.getRefreshExpiredSecond());
      return new Token(accessToken, newRefreshToken);
    } else {
      return new Token(accessToken, token);
    }
  }
}
