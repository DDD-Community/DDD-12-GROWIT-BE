package com.growit.app.user.usecase;

import static com.growit.app.common.util.message.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.promotion.PromotionFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.promotion.Promotion;
import com.growit.app.user.domain.promotion.PromotionRepository;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterPromotionUseCaseTest {

  @Mock private PromotionRepository promotionRepository;
  @Mock private UserRepository userRepository;

  @InjectMocks private RegisterPromotionUseCase registerPromotionUseCase;

  @Test
  void given_ValidPromotionCode_when_RegisterPromotion_then_Success() {
    // given
    User user = UserFixture.defaultUser();
    String promotionCode = "WELCOME2024";
    Promotion promotion = PromotionFixture.validPromotionWithCode(promotionCode);

    given(promotionRepository.findByCode(promotionCode)).willReturn(Optional.of(promotion));

    // when
    registerPromotionUseCase.execute(user, promotionCode);

    // then
    verify(promotionRepository).save(promotion);
    verify(userRepository).saveUser(user);
    assertThat(user.getPromotion()).isEqualTo(promotion);
    assertThat(promotion.isUsed()).isTrue();
  }

  @Test
  void
      given_InvalidPromotionCode_when_RegisterPromotion_then_ThrowPromotionCodeNotFoundException() {
    // given
    User user = UserFixture.defaultUser();
    String invalidCode = "INVALID_CODE";

    given(promotionRepository.findByCode(invalidCode)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> registerPromotionUseCase.execute(user, invalidCode))
        .isInstanceOf(BadRequestException.class)
        .hasMessage(PROMOTION_CODE_NOT_FOUND.getCode());
  }

  @Test
  void given_ExpiredPromotion_when_RegisterPromotion_then_ThrowPromotionCodeInvalidException() {
    // given
    User user = UserFixture.defaultUser();
    String promotionCode = "EXPIRED_PROMO";
    Promotion expiredPromotion = PromotionFixture.expiredPromotionWithCode(promotionCode);

    given(promotionRepository.findByCode(promotionCode)).willReturn(Optional.of(expiredPromotion));

    // when & then
    assertThatThrownBy(() -> registerPromotionUseCase.execute(user, promotionCode))
        .isInstanceOf(BadRequestException.class)
        .hasMessage(PROMOTION_CODE_INVALID.getCode());
  }

  @Test
  void
      given_UserWithActivePromotion_when_RegisterPromotion_then_ThrowPromotionCodeAlreadyExistsException() {
    // given
    User user = UserFixture.userWithPromotion();
    String promotionCode = "NEW_PROMO";
    Promotion promotion = PromotionFixture.validPromotionWithCode(promotionCode);

    given(promotionRepository.findByCode(promotionCode)).willReturn(Optional.of(promotion));

    // when & then
    assertThatThrownBy(() -> registerPromotionUseCase.execute(user, promotionCode))
        .isInstanceOf(BadRequestException.class)
        .hasMessage(PROMOTION_CODE_ALREADY_EXISTS.getCode());
  }

  @Test
  void given_UsedPromotionCode_when_RegisterPromotion_then_ThrowPromotionCodeInvalidException() {
    // given
    User user = UserFixture.defaultUser();
    String promotionCode = "USED_PROMO";
    Promotion usedPromotion = PromotionFixture.usedPromotionWithCode(promotionCode);

    given(promotionRepository.findByCode(promotionCode)).willReturn(Optional.of(usedPromotion));

    // when & then
    assertThatThrownBy(() -> registerPromotionUseCase.execute(user, promotionCode))
        .isInstanceOf(BadRequestException.class)
        .hasMessage(PROMOTION_CODE_INVALID.getCode());
  }
}
