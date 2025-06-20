package com.growit.app.user.domain.token.service;

import com.growit.app.common.config.jwt.JwtProperties;
import com.growit.app.user.domain.token.service.exception.ExpiredTokenException;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;
import io.jsonwebtoken.*;

import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.User;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final JwtProperties jwtProperties;


  private Key getKey() {
    return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();

    } catch (IllegalArgumentException | MalformedJwtException | UnsupportedJwtException e) {
      throw new InvalidTokenException();
    } catch (ExpiredJwtException e) {
      throw new ExpiredTokenException();
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
      .signWith(getKey(), SignatureAlgorithm.HS256)
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


  @Override
  public Token createToken(User user) {
    final Claims claims = createClaim(user.getId());

    final String accessToken = createToken(claims, jwtProperties.getExpiredSecond());
    final String refreshToken = createToken(claims, jwtProperties.getRefreshExpiredSecond());

    return new Token(accessToken, refreshToken);
  }


  public String getId(String token) {
    final Claims claims = parseClaims(token);
    return claims.get("id", String.class);
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
