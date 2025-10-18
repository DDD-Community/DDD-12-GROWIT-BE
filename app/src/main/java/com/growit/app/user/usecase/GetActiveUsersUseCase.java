package com.growit.app.user.usecase;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.userstats.UserStats;
import com.growit.app.user.domain.userstats.UserStatsRepository;
import com.growit.app.user.domain.userstats.service.ActiveUserQuery;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetActiveUsersUseCase {
  private final UserRepository userRepository;
  private final UserStatsRepository userStatsRepository;
  private final ActiveUserQuery activeUserQuery;

  public Page<User> execute(Pageable pageable, int diffDay) {
    final Page<User> users = userRepository.findAll(pageable);

    // 사용자 ID 목록 추출
    final List<String> userIds = users.getContent().stream().map(User::getId).toList();

    // 사용자 통계 조회
    final List<UserStats> userStatsList = userStatsRepository.findByUserIds(userIds);

    // 활성 사용자 ID 필터링 (도메인 서비스 사용)
    final Set<String> activeUserIds = activeUserQuery.getActiveUserIds(userStatsList, diffDay);

    // 활성 사용자만 필터링
    final List<User> filteredUsers =
        users.getContent().stream().filter(user -> activeUserIds.contains(user.getId())).toList();

    // Page 객체로 반환
    return new PageImpl<>(filteredUsers, pageable, filteredUsers.size());
  }
}
