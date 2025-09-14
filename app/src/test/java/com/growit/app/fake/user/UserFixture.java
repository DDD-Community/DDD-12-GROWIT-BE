package com.growit.app.fake.user;

import com.growit.app.user.controller.dto.request.*;
import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.ReIssueCommand;
import com.growit.app.user.domain.user.dto.SignInCommand;
import com.growit.app.user.domain.user.dto.UpdateUserCommand;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;

public class UserFixture {
  public static User defaultUser() {
    return new UserBuilder().build();
  }

  public static Token defaultToken() {
    return new Token("access", "refresh");
  }

  public static UserToken defaultUserToken() {
    return new UserToken("id", "refresh", "userId");
  }

  public static SignInCommand defaultSignInCommand() {
    return new SignInCommand(new Email("test@example.com"), "securePass123");
  }

  public static ReIssueCommand defaultReIssueCommand() {
    return new ReIssueCommand("refresh");
  }

  public static UpdateUserCommand defaultUpdateUserCommand(User user) {
    return new UpdateUserCommand(user, "updatedName", "jobRoleId-1", CareerYear.JUNIOR);
  }

  public static UpdateUserRequest defaultUpdateUserRequest() {
    return new UpdateUserRequest("updatedName", "jobRoleId-1", CareerYear.JUNIOR);
  }

  public static SignUpRequest defaultSignUpRequest() {
    return new SignUpRequest(
        "test@example.com",
        "securePass123",
        "홍길동",
        "6rOg7Zmp7IOd",
        CareerYear.JUNIOR,
        new RequiredConsentRequest(true, true));
  }

  public static SignInRequest defaultSignInRequest() {
    return new SignInRequest("test@example.com", "securePass123");
  }

  public static ReissueRequest defaultReissueRequest() {
    return new ReissueRequest("dummy-refresh-token");
  }

  public static SignUpKaKaoRequest defaultSignUpKaKaoRequest() {
    return new SignUpKaKaoRequest(
        "홍길동",
        "6rOg7Zmp7IOd",
        CareerYear.JUNIOR,
        new RequiredConsentRequest(true, true),
        "dummy-registration-token");
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
