package com.growit.app.user.domain.user;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
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

  public static String generateId() {
    return NanoIdUtils.randomNanoId();
  }

  public static User from(SignUpCommand command) {
    return User.builder()
        .id(generateId())
        .email(command.email())
        .password(command.password())
        .name(command.name())
        .jobRoleId(command.jobRoleId())
        .careerYear(command.careerYear())
        .build();
  }
}
