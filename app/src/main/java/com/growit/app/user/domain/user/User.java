package com.growit.app.user.domain.user;

import com.growit.app.common.util.IDGenerator;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.dto.UpdateUserCommand;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.RequiredConsent;
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

  public static User from(SignUpCommand command) {
    return User.builder()
        .id(IDGenerator.generateId())
        .email(command.email())
        .password(command.password())
        .name(command.name())
        .jobRoleId(command.jobRoleId())
        .careerYear(command.careerYear())
        .isDeleted(false)
        .build();
  }

  public void updateByCommand(UpdateUserCommand command) {
    this.name = command.name();
    this.jobRoleId = command.jobRoleId();
    this.careerYear = command.careerYear();
  }

  public void deleted() {
    this.isDeleted = true;
  }
}
