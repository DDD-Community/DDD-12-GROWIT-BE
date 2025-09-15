package com.growit.app.user.domain.user;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.dto.UpdateUserCommand;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;
import com.growit.app.user.domain.user.vo.RequiredConsent;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {
  private String id;

  private Email email;

  private String password;

  private String name;

  private String jobRoleId;

  private CareerYear careerYear;

  private RequiredConsent requiredConsent;

  private boolean isDeleted;

  private boolean isOnboarding;
  private ArrayList<OAuth> oauthAccounts;

  public static User from(SignUpCommand command) {

    return User.builder()
        .id(IDGenerator.generateId())
        .email(command.email())
        .password(command.password())
        .name(command.name())
        .jobRoleId(command.jobRoleId())
        .careerYear(command.careerYear())
        .isOnboarding(false)
        .isDeleted(false)
        .oauthAccounts(
            command.oAuth() == null ? new ArrayList<>() : new ArrayList<>(List.of(command.oAuth())))
        .build();
  }

  public void updateByCommand(UpdateUserCommand command) {
    this.name = command.name();
    this.jobRoleId = command.jobRoleId();
    this.careerYear = command.careerYear();
  }

  public boolean hasAnyOAuth() {
    return oauthAccounts != null && !oauthAccounts.isEmpty();
  }

  public boolean hasProvider(String provider) {
    return oauthAccounts != null
        && oauthAccounts.stream().anyMatch(o -> o.provider().equals(provider));
  }

  public void linkOAuth(String provider, String providerId) {
    if (hasProvider(provider)) {
      throw new BadRequestException("해당 provider가 이미 연결되어 있습니다.");
    }
    if (oauthAccounts == null) {
      oauthAccounts = new ArrayList<>();
    }
    oauthAccounts.add(new OAuth(provider, providerId));
  }

  public void deleted() {
    this.isDeleted = true;
  }

  public void onboarding() {
    this.isOnboarding = true;
  }
}
