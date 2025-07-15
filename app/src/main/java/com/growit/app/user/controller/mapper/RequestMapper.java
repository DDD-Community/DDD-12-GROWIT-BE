package com.growit.app.user.controller.mapper;

import com.growit.app.user.controller.dto.request.*;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.*;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {
  public SignUpCommand toSignUpCommand(SignUpRequest request) {
    return new SignUpCommand(
        new Email(request.getEmail()),
        request.getPassword(),
        request.getName(),
        request.getJobRoleId(),
        CareerYear.valueOf(request.getCareerYear().toUpperCase()));
  }

  public SignInCommand toSignInCommand(SignInRequest request) {
    return new SignInCommand(new Email(request.getEmail()), request.getPassword());
  }

  public ReIssueCommand toReIssueCommand(ReissueRequest request) {
    return new ReIssueCommand(request.getRefreshToken());
  }

  public RequiredConsentCommand toRequiredConsentCommand(RequiredConsentRequest request) {
    return new RequiredConsentCommand(
        request.isPrivacyPolicyAgreed(), request.isServiceTermsAgreed());
  }

  public UpdateUserCommand toUpdateUserCommand(User user, UpdateUserRequest request) {
    return new UpdateUserCommand(
        user, request.getName(), request.getJobRoleId(), request.getCareerYear());
  }
}
