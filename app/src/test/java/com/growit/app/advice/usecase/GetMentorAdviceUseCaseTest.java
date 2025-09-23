package com.growit.app.advice.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.advice.MentorAdviceFixture;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.user.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMentorAdviceUseCaseTest {

  @Mock private MentorAdviceRepository mentorAdviceRepository;
  @Mock private GetUserGoalsUseCase getUserGoalsUseCase;

  @InjectMocks private GetMentorAdviceUseCase getMentorAdviceUseCase;

  @Test
  void givenNoActiveGoal_whenExecute_thenThrowNotFoundException() {
    // given
    User user = UserFixture.defaultUser();
    given(getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS)).willReturn(List.of());

    // when & then
    assertThatThrownBy(() -> getMentorAdviceUseCase.execute(user))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("현재 진행중인 목표가 존재하지 않습니다.");
  }

  @Test
  void givenNoExistingAdvice_whenExecute_thenReturnNull() {
    // given
    User user = UserFixture.defaultUser();
    Goal goal = GoalFixture.defaultGoal();
    given(getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS)).willReturn(List.of(goal));
    given(mentorAdviceRepository.findByUserIdAndGoalId(user.getId(), goal.getId()))
        .willReturn(Optional.empty());

    // when
    MentorAdvice result = getMentorAdviceUseCase.execute(user);

    // then
    assertThat(result).isNull();
  }

  @Test
  void givenUncheckedAdvice_whenExecute_thenUpdateToCheckedAndSave() {
    // given
    User user = UserFixture.defaultUser();
    Goal goal = GoalFixture.defaultGoal();
    MentorAdvice mentorAdvice = MentorAdviceFixture.uncheckedMentorAdvice();

    given(getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS)).willReturn(List.of(goal));
    given(mentorAdviceRepository.findByUserIdAndGoalId(user.getId(), goal.getId()))
        .willReturn(Optional.of(mentorAdvice));

    // when
    MentorAdvice result = getMentorAdviceUseCase.execute(user);

    // then
    assertThat(result.isChecked()).isTrue();
    then(mentorAdviceRepository).should().save(mentorAdvice);
  }

  @Test
  void givenAlreadyCheckedAdvice_whenExecute_thenDoNotSave() {
    // given
    User user = UserFixture.defaultUser();
    Goal goal = GoalFixture.defaultGoal();
    MentorAdvice mentorAdvice = MentorAdviceFixture.checkedMentorAdvice();

    given(getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS)).willReturn(List.of(goal));
    given(mentorAdviceRepository.findByUserIdAndGoalId(user.getId(), goal.getId()))
        .willReturn(Optional.of(mentorAdvice));

    // when
    MentorAdvice result = getMentorAdviceUseCase.execute(user);

    // then
    assertThat(result.isChecked()).isTrue();
    then(mentorAdviceRepository).shouldHaveNoMoreInteractions();
  }
}
