package com.growit.app.user.usecase;

import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignInUseCase {
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;

  public Token execute(Email email, String password) {
    final User user = userRepository.findByEmail(email).orElseThrow();
    final boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());
    if (!isPasswordCorrect) throw new RuntimeException();

    return tokenService.createToken(user);
  }
}
