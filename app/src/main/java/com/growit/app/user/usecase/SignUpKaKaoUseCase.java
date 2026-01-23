package com.growit.app.user.usecase;

import com.growit.app.resource.domain.jobrole.service.JobRoleValidator;
import com.growit.app.user.domain.token.service.JwtClaimKeys;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.RequiredConsentCommand;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.dto.SignUpKaKaoCommand;
import com.growit.app.user.domain.user.service.UserValidator;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpKaKaoUseCase {
  private final UserRepository userRepository;
  private final JobRoleValidator jobRoleValidator;
  private final UserValidator userValidator;
  private final TokenService tokenService;

  @Transactional
  public void execute(
      SignUpKaKaoCommand signUpCommand, RequiredConsentCommand requiredConsentCommand) {
    requiredConsentCommand.checkRequiredConsent();
    // 1) registrationToken 검증 및 클레임 추출 (provider, providerId, email)
    Claims claims = tokenService.parseClaims(signUpCommand.registrationToken());
    String provider = claims.get(JwtClaimKeys.PROVIDER, String.class);
    String providerId = claims.get(JwtClaimKeys.PROVIDER_ID, String.class);
    String emailFromToken = claims.get(JwtClaimKeys.EMAIL, String.class);

    // 3) OAuth VO 구성
    OAuth oAuth = new OAuth(provider, providerId);

    // 4) 최종 SignUpCommand 조립 (소셜 가입은 비밀번호가 없으므로 빈 문자열로 처리)
    SignUpCommand command =
        new SignUpCommand(
            new Email(emailFromToken),
            null,
            signUpCommand.name(),
            null,
            signUpCommand.jobRoleId(),
            signUpCommand.careerYear(),
            oAuth);

    jobRoleValidator.checkJobRoleExist(command.jobRoleId());
    userValidator.checkEmailExists(command.email());
    User user = User.from(command);

    userRepository.saveUser(user);
  }
}
