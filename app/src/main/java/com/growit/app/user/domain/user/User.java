package com.growit.app.user.domain.user;

import com.growit.app.common.util.IdGenerator;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
  private String id;
  private Email email;
  private String password;
  private String name;
  private String jobRoleId;
  private CareerYear careerYear;

  public static User from(SignUpCommand command) {
    return User.builder()
      .id(IdGenerator.generateId())
      .email(command.email())
      .password(command.password())
      .name(command.name())
      .jobRoleId(command.jobRoleId())
      .careerYear(command.careerYear())
      .build();
  }
}
