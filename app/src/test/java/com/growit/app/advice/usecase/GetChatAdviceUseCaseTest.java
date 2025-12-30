package com.growit.app.advice.usecase;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.useradvicestatus.UserAdviceStatus;
import com.growit.app.user.domain.useradvicestatus.repository.UserAdviceStatusRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetChatAdviceUseCaseTest {

  @Mock private UserAdviceStatusRepository userAdviceStatusRepository;
  @Mock private ChatAdviceRepository chatAdviceRepository;
  @Mock private ChatAdviceService chatAdviceService;

  @InjectMocks private GetChatAdviceUseCase getChatAdviceUseCase;

  @Test
  @DisplayName("조언 조회 시 reset 체크 로직이 호출되어야 한다")
  void givenUserAndAdvice_whenExecute_thenResetDailyLimitShouldBeCalled() {
    // given
    User user = User.builder().id("user-1").build();
    ChatAdvice chatAdvice =
        ChatAdvice.builder().userId("user-1").id(1L).lastResetDate(LocalDate.now()).build();
    UserAdviceStatus status = new UserAdviceStatus("user-1", false);

    given(chatAdviceRepository.findByUserId("user-1")).willReturn(Optional.of(chatAdvice));
    given(userAdviceStatusRepository.findByUserId("user-1")).willReturn(Optional.of(status));
    given(chatAdviceService.checkAndResetDailyLimit(chatAdvice)).willReturn(chatAdvice);

    // when
    getChatAdviceUseCase.execute(user, 1);

    // then
    verify(chatAdviceService).checkAndResetDailyLimit(chatAdvice);
  }
}
