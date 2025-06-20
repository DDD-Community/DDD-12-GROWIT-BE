package com.growit.app.user.usecase;

import com.growit.app.common.exception.BaseException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.user.controller.dto.request.SignInRequest;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.SignInCommand;
import com.growit.app.user.domain.user.vo.Email;
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
  public Token execute(SignInRequest request) throws BaseException {
    SignInCommand command = new SignInCommand(new Email(request.getEmail()), request.getPassword());
    User user =
        userRepository
            .findByEmail(command.email())
            .orElseThrow(() -> new NotFoundException("로그인 정보를 확인해주세요"));
    boolean isCorrectPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (!isCorrectPassword) throw new NotFoundException("로그인 정보를 확인해주세요");

    return tokenService.createToken(user);
  }
}
