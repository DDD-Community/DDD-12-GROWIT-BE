package com.growit.app.user.usecase;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.OAuthCommand;
import com.growit.app.user.domain.user.vo.Email;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuthLinkUseCase {

  private final UserRepository userRepository;

  @Transactional
  public Optional<User> execute(OAuthCommand command) {
    Optional<User> existingUser = userRepository.findByEmail(new Email(command.email()));
    if (existingUser.isPresent()) {
      User user = existingUser.get();

      if (!user.hasProvider(command.provider())) {
        if (user.hasAnyOAuth()) {
          String linkedProvider = user.getOauthAccounts().get(0).provider();
          throw new com.growit.app.common.exception.BadRequestException(
              "이미 " + linkedProvider + " 계정으로 가입된 이메일입니다.");
        }
        user.linkOAuth(command.provider(), command.providerId(), command.refreshToken());
        userRepository.saveUser(user);
      } else if (command.refreshToken() != null) {
        user.updateOAuthRefreshToken(command.provider(), command.refreshToken());
        userRepository.saveUser(user);
      }

      return existingUser;
    }

    return Optional.empty();
  }
}
