package com.growit.app.advice.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.fake.advice.MentorAdviceFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.user.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMentorAdviceUseCaseTest {

  @Mock private MentorAdviceRepository mentorAdviceRepository;
  @Mock private GetUserGoalsUseCase getUserGoalsUseCase;
  @Mock private GenerateMentorAdviceUseCase generateMentorAdviceUseCase;

  @InjectMocks private GetMentorAdviceUseCase getMentorAdviceUseCase;

  @Test
  void givenUser_whenExecute_thenReturnGeneratedAdvice() {
    // given
    User user = UserFixture.defaultUser();
    MentorAdvice expectedAdvice = MentorAdviceFixture.defaultMentorAdvice();

    given(generateMentorAdviceUseCase.execute(user)).willReturn(expectedAdvice);

    // when
    MentorAdvice result = getMentorAdviceUseCase.execute(user);

    // then
    assertThat(result).isEqualTo(expectedAdvice);
    then(generateMentorAdviceUseCase).should().execute(user);
  }
}
