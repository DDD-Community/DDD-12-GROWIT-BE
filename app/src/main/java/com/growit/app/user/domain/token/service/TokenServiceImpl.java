package com.growit.app.user.domain.token.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {
  private String secretKey;
  @Override
  public Token createToken(User user) {
    long now = System.currentTimeMillis();
    long accessTokenValidity = 1000 * 60 * 60; // 1시간
    long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 1주
    String accessToken = Jwts.builder()
      .setSubject(user.getEmail().toString())
      .claim("userId", user.getId())
      .setIssuedAt(new Date(now))
      .setExpiration(new Date(now + accessTokenValidity))
      .signWith(SignatureAlgorithm.HS256, secretKey)
      .compact();

    String refreshToken = Jwts.builder()
      .setSubject(user.getEmail().toString())
      .setIssuedAt(new Date(now))
      .setExpiration(new Date(now + refreshTokenValidity))
      .signWith(SignatureAlgorithm.HS256, secretKey)
      .compact();

    return new Token(accessToken, refreshToken);
  }
}
