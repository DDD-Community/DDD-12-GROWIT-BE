package com.growit.app.common.config.oauth;

import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class OAuth2UserRegistrar {

  private final UserRepository userRepository;

  public OAuth2UserRegistrar(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public UserDomainPrincipal registerOrLoad(String provider, KakaoOAuth2UserService.KakaoProfile profile) {
    // 1) 소셜 계정 존재 여부 확인 (provider + providerId)
    Optional<User> foundAccount = userRepository.findByProviderAndProviderId(provider, profile.getProviderId());
    User user = null;
    if (foundAccount.isPresent()) {
      // 기존 연결된 사용자 반환
      user = foundAccount.get();
    } else {
      // 2) 이메일 제공 시 기존 사용자와 병합 (보수적으로 이메일이 존재하는 경우에만)
        Optional<User> byEmail = userRepository.findByEmail(new Email(profile.getEmail()));
        if (byEmail.isPresent()) {
          user = byEmail.get();
        }
//        else {
//          // 3) 신규 사용자 생성
//          user = new User();
//          user.setEmail(profile.getEmail()); // null 가능성 고려
//          user.setName(profile.getNickname());
//          user.setProfileImageUrl(profile.getProfileImage());
//          user.setRole(Role.USER);
//          user = userRepository.save(user);
//        }
    }

    return UserDomainPrincipal.from(user);
  }
}
