package com.growit.app.common.config.oauth;

import com.growit.app.common.exception.NotFoundException;
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
  /**
   * 기존 사용자 조회 전용 메서드 (지연 가입 플로우)
   * provider+providerId 또는 email 기준으로 기존 User를 찾는다. 저장하지 않는다.
   */
  @Transactional(readOnly = true)
  public Optional<User> findExistingUser(String provider, KakaoOAuth2UserService.KakaoProfile profile) {
    // 1) provider + providerId 로 조회
    Optional<User> byAccount = userRepository.findExistingUser(provider, profile.getProviderId());
    if (byAccount.isPresent()) return byAccount;

    // 2) 이메일이 있는 경우 이메일로 조회 (보수적으로)
    if (profile.getEmail() != null && !profile.getEmail().isBlank()) {
      return userRepository.findByEmail(new Email(profile.getEmail()));
    }
    return Optional.empty();
  }
}
