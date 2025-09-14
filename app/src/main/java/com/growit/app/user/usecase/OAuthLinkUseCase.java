package com.growit.app.user.usecase;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.OAuthCommand;
import com.growit.app.user.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthLinkUseCase {

  private final UserRepository userRepository;

  @Transactional
  public Optional<User> execute(OAuthCommand command) {
    Optional<User> existingOAuthUser = userRepository.findExistingUser(
        command.provider(),
        command.providerId()
    );

    if (existingOAuthUser.isPresent()) {
      return existingOAuthUser;
    }

    if (command.email() != null) {
      return linkExistingUserIfPresent(command);
    }

    return Optional.empty();
  }

  private Optional<User> linkExistingUserIfPresent(OAuthCommand command) {
    Optional<User> existingUser = userRepository.findByEmail(new Email(command.email()));
    
    if (existingUser.isEmpty()) {
      return Optional.empty();
    }
    
    User user = existingUser.get();
    if (!user.hasAnyOAuth()) {
      user.linkOAuth(command.provider(), command.providerId());
      userRepository.saveUser(user);
    }
    
    return Optional.of(user);
  }
}
