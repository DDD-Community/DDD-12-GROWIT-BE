package com.growit.app.user.usecase;

import static com.growit.app.common.util.message.ErrorCode.USER_SIGN_IN_FAILED;

import com.growit.app.common.exception.BaseException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.SignInCommand;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignInUseCase {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  @Transactional
  public Token execute(SignInCommand command) throws BaseException {
    User user =
        userRepository
            .findByEmail(command.email())
            .orElseThrow(() -> new NotFoundException(USER_SIGN_IN_FAILED.getCode()));

    boolean isCorrectPassword = passwordEncoder.matches(command.password(), user.getPassword());
    if (!isCorrectPassword) throw new NotFoundException(USER_SIGN_IN_FAILED.getCode());

    return tokenService.createToken(user);
  }
}
