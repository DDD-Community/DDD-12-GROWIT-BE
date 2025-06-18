package com.growit.app.user.domain.user;

import com.growit.app.common.util.IdGenerator;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import lombok.*;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
