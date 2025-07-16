package com.growit.app.user.domain.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.user.FakeUserRepository;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private final User user = UserFixture.defaultUser();
  private UserValidator userService;

  @BeforeEach
  void setUp() {
    FakeUserRepository fakeUserRepository = new FakeUserRepository();
    userService = new UserService(fakeUserRepository);
    fakeUserRepository.saveUser(user);
  }

  @Test
  void givenExistingEmail_whenCheckEmailExists_thenThrowAlreadyExistsEmailException() {
    // given
    Email email = user.getEmail();

    // when & then
    assertThatThrownBy(() -> userService.checkEmailExists(email))
        .isInstanceOf(AlreadyExistEmailException.class);
  }

  @Test
  void givenNonExistingEmail_whenCheckEmailExists_thenNoException() {
    // given
    Email email = new Email("newuser@example.com");

    // when & then
    userService.checkEmailExists(email); // no exception expected
  }

  @Test
  void givenInvalidEmailFormat_whenCreateEmail_thenThrowIllegalArgumentException() {
    // given
    String invalidEmail = "invalid-email";

    // when & then
    assertThatThrownBy(() -> new Email(invalidEmail)).isInstanceOf(BadRequestException.class);
  }
}
