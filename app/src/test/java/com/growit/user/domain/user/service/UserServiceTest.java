package com.growit.user.domain.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.growit.common.exception.BadRequestException;
import com.growit.fake.user.FakeUserRepository;
import com.growit.fake.user.UserFixture;
import com.growit.user.domain.user.User;
import com.growit.user.domain.user.vo.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private final User user = UserFixture.defaultUser();
  private UserService userService;

  @BeforeEach
  void setUp() {
    FakeUserRepository fakeUserRepository = new FakeUserRepository();
    userService = new UserServiceImpl(fakeUserRepository);
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
