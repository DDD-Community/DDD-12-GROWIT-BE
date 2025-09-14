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
    Optional<User> existingUser = userRepository.findByEmail(new Email(command.email()));
    if (existingUser.isPresent()) {
      User user = existingUser.get();
      if(!existingUser.get().hasAnyOAuth()) {
        user.linkOAuth(command.provider(), command.providerId());
        userRepository.saveUser(user);
      }

      return existingUser;
    }

    return Optional.empty();
  }
}
