package com.growit.app.user.controller.mapper;

import com.growit.app.user.controller.dto.request.ReissueRequest;
import com.growit.app.user.controller.dto.request.RequiredConsentRequest;
import com.growit.app.user.controller.dto.request.SignInRequest;
import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.controller.dto.request.UpdateUserRequest;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.*;
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
        request.getCareerYear());
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
