package com.growit.app.user.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.growit.app.fake.user.UserFixture;
import com.growit.app.fake.user.UserStatsFixture;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.userstats.UserStats;
import com.growit.app.user.domain.userstats.UserStatsRepository;
import com.growit.app.user.domain.userstats.service.ActiveUserQuery;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class GetActiveUsersUseCaseTest {

  @Mock private UserRepository userRepository;
  @Mock private UserStatsRepository userStatsRepository;
  @Mock private ActiveUserQuery activeUserQuery;

  @InjectMocks private GetActiveUsersUseCase getActiveUsersUseCase;

  @Test
  @DisplayName("활성 사용자가 없을 때 빈 Page를 반환한다")
  void givenNoActiveUsers_whenExecute_thenReturnsEmptyPage() {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    int diffDay = 1;

    User user1 = UserFixture.userWithId("user-1");
    List<User> allUsers = List.of(user1);
    Page<User> userPage = new PageImpl<>(allUsers, pageable, allUsers.size());

    List<String> userIds = List.of("user-1");
    UserStats notAccessedUserStats = UserStatsFixture.notAccessedForThreeDaysUserStats();
    List<UserStats> userStatsList = List.of(notAccessedUserStats);

    Set<String> activeUserIds = Set.of(); // 활성 사용자 없음

    given(userRepository.findAll(pageable)).willReturn(userPage);
    given(userStatsRepository.findByUserIds(userIds)).willReturn(userStatsList);
    given(activeUserQuery.getActiveUserIds(userStatsList, diffDay)).willReturn(activeUserIds);

    // when
    Page<User> result = getActiveUsersUseCase.execute(pageable, diffDay);

    // then
    assertThat(result.getContent()).isEmpty();
  }

  @Test
  @DisplayName("사용자가 없을 때 빈 Page를 반환한다")
  void givenNoUsers_whenExecute_thenReturnsEmptyPage() {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    int diffDay = 1;

    List<User> allUsers = List.of();
    Page<User> userPage = new PageImpl<>(allUsers, pageable, 0);

    List<String> userIds = List.of();
    List<UserStats> userStatsList = List.of();
    Set<String> activeUserIds = Set.of();

    given(userRepository.findAll(pageable)).willReturn(userPage);
    given(userStatsRepository.findByUserIds(userIds)).willReturn(userStatsList);
    given(activeUserQuery.getActiveUserIds(userStatsList, diffDay)).willReturn(activeUserIds);

    // when
    Page<User> result = getActiveUsersUseCase.execute(pageable, diffDay);

    // then
    assertThat(result.getContent()).isEmpty();
  }
}
