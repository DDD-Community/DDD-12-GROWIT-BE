package com.growit.app.fake;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;

public class UserFixture {
  public static User defaultUser() {
    return new UserBuilder().build();
  }
}

class UserBuilder {
  private String id = "user-1";
  private String email = "user@example.com";
  private String password = "encodedPassword";
  private String name = "testUser";
  private String jobRoleId = "jobRoleId123";
  private CareerYear careerYear = CareerYear.JUNIOR;

  public UserBuilder withId(String id) {
    this.id = id;
    return this;
  }

  public UserBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public UserBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public UserBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public UserBuilder withJobRoleId(String jobRoleId) {
    this.jobRoleId = jobRoleId;
    return this;
  }

  public UserBuilder withCareerYear(CareerYear careerYear) {
    this.careerYear = careerYear;
    return this;
  }

  public User build() {
    return User.builder()
        .id(id)
        .email(new Email(email))
        .password(password)
        .name(name)
        .jobRoleId(jobRoleId)
        .careerYear(careerYear)
        .build();
  }
}
