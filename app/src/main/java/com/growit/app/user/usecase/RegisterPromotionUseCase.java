package com.growit.app.user.usecase;

import static com.growit.app.common.util.message.ErrorCode.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.user.domain.promotion.Promotion;
import com.growit.app.user.domain.promotion.PromotionRepository;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegisterPromotionUseCase {
  private final PromotionRepository promotionRepository;
  private final UserRepository userRepository;

  @Transactional
  public void execute(User user, String promotionCode) {
    // 1. Promotion 코드로 조회
    Promotion promotion =
        promotionRepository
            .findByCode(promotionCode)
            .orElseThrow(() -> new BadRequestException(PROMOTION_CODE_NOT_FOUND.getCode()));

    // 2. 프로모션이 유효한지 확인
    if (!promotion.isValid()) {
      throw new BadRequestException(PROMOTION_CODE_INVALID.getCode());
    }

    if (user.hasActivePromotion()) {
      throw new BadRequestException(PROMOTION_CODE_ALREADY_EXISTS.getCode());
    }
    // 3. 프로모션을 사용됨으로 표시
    promotion.markAsUsed();

    // 4. User에 프로모션 설정
    user.addPromotion(promotion);

    // 5. 프로모션과 User 저장
    promotionRepository.save(promotion);
    userRepository.saveUser(user);
  }
}
