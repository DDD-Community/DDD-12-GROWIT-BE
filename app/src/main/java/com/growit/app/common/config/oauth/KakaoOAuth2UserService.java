package com.growit.app.common.config.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

// KakaoOAuth2UserService.java
@Component
@RequiredArgsConstructor
public class KakaoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final OAuth2UserRegistrar oAuth2UserRegistrar;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(req);
        String registrationId = req.getClientRegistration().getRegistrationId(); // "kakao"

        Map<String, Object> attributes = oAuth2User.getAttributes();
        KakaoProfile profile = KakaoProfile.from(attributes);

        // 가입 or 로그인 처리 (도메인 User 생성/조회)
        UserDomainPrincipal principal = oAuth2UserRegistrar.registerOrLoad(registrationId, profile);

        return new DefaultOAuth2User(
            principal.getAuthorities(),
            attributes,
            "id" // user-name-attribute (provider에 맞춤)
        );
    }

    @Getter
    @Builder
    public static class KakaoProfile {
        private String provider;  // "kakao"
        private String providerId; // id
        private String email;
        private String nickname;
        private String profileImage;

        @SuppressWarnings("unchecked")
        public static KakaoProfile from(Map<String, Object> attrs) {
            Map<String, Object> account = (Map<String, Object>) attrs.getOrDefault("kakao_account", Map.of());
            Map<String, Object> profile = (Map<String, Object>) account.getOrDefault("profile", Map.of());

            return KakaoProfile.builder()
                .provider("kakao")
                .providerId(String.valueOf(attrs.get("id")))
                .email((String) account.get("email"))
                .nickname((String) profile.get("nickname"))
                .profileImage((String) profile.get("profile_image_url"))
                .build();
        }
    }
}
