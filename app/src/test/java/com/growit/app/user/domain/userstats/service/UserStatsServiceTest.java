package com.growit.app.user.domain.userstats.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.growit.app.fake.user.UserStatsFixture;
import com.growit.app.user.domain.userstats.UserStats;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserStatsServiceTest {

  private UserStatsService userStatsService;

  @BeforeEach
  void setUp() {
    userStatsService = new UserStatsService();
  }

  @Test
  @DisplayName("1일 이내 접속한 활성 사용자들의 ID를 반환한다")
  void getActiveUserIds_WithDiffDay1_ReturnsActiveUserIds() {
    // given
    UserStats activeUser1 = new UserStats("user-1", LocalDate.now(), 1);
    UserStats activeUser2 = new UserStats("user-2", LocalDate.now().minusDays(1), 2);
    UserStats inactiveUser = new UserStats("user-3", LocalDate.now().minusDays(2), 1);

    List<UserStats> userStatsList = List.of(activeUser1, activeUser2, inactiveUser);
    int diffDay = 1;

    // when
    Set<String> result = userStatsService.getActiveUserIds(userStatsList, diffDay);

    // then
    assertThat(result).containsExactlyInAnyOrder("user-1", "user-2");
  }

  @Test
  @DisplayName("lastSeenDate가 null인 사용자는 활성 사용자에서 제외된다")
  void getActiveUserIds_WithNullLastSeenDate_ExcludesFromActiveUsers() {
    // given
    UserStats activeUser = new UserStats("user-1", LocalDate.now(), 1);
    UserStats nullLastSeenUser = new UserStats("user-2", null, 0);

    List<UserStats> userStatsList = List.of(activeUser, nullLastSeenUser);
    int diffDay = 1;

    // when
    Set<String> result = userStatsService.getActiveUserIds(userStatsList, diffDay);

    // then
    assertThat(result).containsExactly("user-1");
  }

  @Test
  @DisplayName("diffDay가 0일 때 오늘 접속한 사용자만 반환한다")
  void getActiveUserIds_WithDiffDay0_ReturnsTodayActiveUsers() {
    // given
    UserStats todayUser = new UserStats("user-1", LocalDate.now(), 1);
    UserStats yesterdayUser = new UserStats("user-2", LocalDate.now().minusDays(1), 1);

    List<UserStats> userStatsList = List.of(todayUser, yesterdayUser);
    int diffDay = 0;

    // when
    Set<String> result = userStatsService.getActiveUserIds(userStatsList, diffDay);

    // then
    assertThat(result).containsExactly("user-1");
  }

  @Test
  @DisplayName("diffDay가 3일 때 3일 이내 접속한 사용자들을 반환한다")
  void getActiveUserIds_WithDiffDay3_ReturnsUsersWithin3Days() {
    // given
    UserStats user1 = new UserStats("user-1", LocalDate.now(), 1);
    UserStats user2 = new UserStats("user-2", LocalDate.now().minusDays(2), 1);
    UserStats user3 = new UserStats("user-3", LocalDate.now().minusDays(3), 1);
    UserStats user4 = new UserStats("user-4", LocalDate.now().minusDays(4), 1);

    List<UserStats> userStatsList = List.of(user1, user2, user3, user4);
    int diffDay = 3;

    // when
    Set<String> result = userStatsService.getActiveUserIds(userStatsList, diffDay);

    // then
    assertThat(result).containsExactlyInAnyOrder("user-1", "user-2", "user-3");
  }

  @Test
  @DisplayName("빈 사용자 통계 리스트에 대해 빈 Set을 반환한다")
  void getActiveUserIds_WithEmptyList_ReturnsEmptySet() {
    // given
    List<UserStats> emptyList = List.of();
    int diffDay = 1;

    // when
    Set<String> result = userStatsService.getActiveUserIds(emptyList, diffDay);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("fixture를 사용한 테스트 - 일반 사용자는 활성 사용자로 포함된다")
  void getActiveUserIds_WithFixture_IncludesNormalUser() {
    // given
    UserStats normalUser = UserStatsFixture.normalUserStats();
    UserStats notAccessedUser = UserStatsFixture.notAccessedForThreeDaysUserStats();

    List<UserStats> userStatsList = List.of(normalUser, notAccessedUser);
    int diffDay = 1;

    // when
    Set<String> result = userStatsService.getActiveUserIds(userStatsList, diffDay);

    // then
    assertThat(result).containsExactly("user-1");
  }
}
