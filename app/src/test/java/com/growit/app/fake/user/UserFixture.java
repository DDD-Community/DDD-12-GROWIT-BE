package com.growit.app.fake.user;

import com.growit.app.user.controller.dto.request.ReissueRequest;
import com.growit.app.user.controller.dto.request.RequiredConsentRequest;
import com.growit.app.user.controller.dto.request.SignInRequest;
import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;

public class UserFixture {
  public static User defaultUser() {
    return new UserBuilder().build();
  }

  public static SignUpRequest defaultSignUpRequest() {
    return new SignUpRequest(
        "test@example.com",
        "securePass123",
        "홍길동",
        "6rOg7Zmp7IOd",
        CareerYear.JUNIOR.name(),
        new RequiredConsentRequest(true, true));
  }

  public static SignInRequest defaultSignInRequest() {
    return new SignInRequest("test@example.com", "securePass123");
  }

  public static ReissueRequest defaultReissueRequest() {
    return new ReissueRequest("dummy-refresh-token");
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
