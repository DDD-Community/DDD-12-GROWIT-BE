package com.growit.app.user.domain.token.service;

public final class JwtClaimKeys {
  // JWT Claim Keys
  public static final String PROVIDER = "provider";
  public static final String PROVIDER_ID = "providerId";
  public static final String EMAIL = "email";
  public static final String TYPE = "type";
  public static final String ID = "id";
  public static final String JTI = "jti";

  // OAuth Attribute Keys
  public static final String PENDING_SIGNUP = "pendingSignup";
  public static final String USER = "user";
  public static final String NICK_NAME = "nickName";

  private JwtClaimKeys() {
    // utility class
  }
}
