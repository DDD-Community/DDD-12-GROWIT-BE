package com.growit.app.common.config.oauth;

public final class KakaoKeys {
  // Kakao OAuth Response Keys
  public static final String KAKAO_ACCOUNT = "kakao_account";
  public static final String PROFILE = "profile";
  public static final String NICKNAME = "nickname";
  public static final String PROFILE_IMAGE_URL = "profile_image_url";
  public static final String ID = "id";
  public static final String EMAIL = "email";

  // OIDC Claims
  public static final String SUB = "sub";
  public static final String ISSUER = "https://kauth.kakao.com";
  public static final String NONCE = "nonce";

  // OAuth Authorization URLs
  public static final String KAKAO_AUTHORIZATION_URL = "/oauth2/authorization/kakao";

  // Provider Name
  public static final String PROVIDER_NAME = "kakao";

  private KakaoKeys() {
    // utility class
  }
}
