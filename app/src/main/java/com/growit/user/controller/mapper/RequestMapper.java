package com.growit.user.controller.mapper;

import com.growit.user.controller.dto.request.ReissueRequest;
import com.growit.user.controller.dto.request.RequiredConsentRequest;
import com.growit.user.controller.dto.request.SignInRequest;
import com.growit.user.controller.dto.request.SignUpRequest;
import com.growit.user.domain.user.dto.ReIssueCommand;
import com.growit.user.domain.user.dto.RequiredConsentCommand;
import com.growit.user.domain.user.dto.SignInCommand;
import com.growit.user.domain.user.dto.SignUpCommand;
import com.growit.user.domain.user.vo.CareerYear;
import com.growit.user.domain.user.vo.Email;
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
}
