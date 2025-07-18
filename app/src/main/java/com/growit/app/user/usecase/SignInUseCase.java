package com.growit.app.user.usecase;

import static com.growit.app.common.util.message.ErrorCode.USER_SIGN_IN_FAILED;

import com.growit.app.common.exception.BaseException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.UserTokenSaver;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.SignInCommand;
import com.growit.app.user.domain.user.service.UserQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignInUseCase {
  private final UserQuery userQuery;
  private final UserTokenSaver userTokenSaver;
  private final PasswordEncoder passwordEncoder;
  private final TokenGenerator tokenGenerator;

  @Transactional
  public Token execute(SignInCommand command) throws BaseException {
    final User user = userQuery.getUserByEmail(command.email());
    final boolean isCorrectPassword =
        passwordEncoder.matches(command.password(), user.getPassword());
    if (!isCorrectPassword) throw new NotFoundException(USER_SIGN_IN_FAILED.getCode());

    final Token token = tokenGenerator.createToken(user);
    userTokenSaver.saveUserToken(user.getId(), token);

    return token;
  }
}
