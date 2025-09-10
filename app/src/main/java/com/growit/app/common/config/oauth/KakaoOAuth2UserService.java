package com.growit.app.common.config.oauth;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final OAuth2UserRegistrar userRegistrar;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(req);
        String registrationId = req.getClientRegistration().getRegistrationId(); // "kakao"

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        KakaoProfile profile = KakaoProfile.from(attributes);

        // 가입 지연(Deferred Signup) 처리: 기존 사용자만 주입, 없으면 pendingSignup 플래그로 반환
        Optional<User> existing = userRegistrar.findExistingUser(registrationId, profile);
        if (existing.isPresent()) {
            User user = existing.get();
            attributes.put("user", user);
            return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("USER")),
                attributes,
                "id"
            );
        } else {
            attributes.put("pendingSignup", true);
            attributes.put("provider", "kakao");
            attributes.put("providerId", profile.getProviderId());
            attributes.put("email", profile.getEmail());
          attributes.put("nickName", profile.getNickname());

          return new DefaultOAuth2User(
                List.of(),
                attributes,
                "id"
            );
        }
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
