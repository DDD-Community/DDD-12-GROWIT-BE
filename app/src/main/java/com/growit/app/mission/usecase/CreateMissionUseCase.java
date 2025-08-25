package com.growit.app.mission.usecase;

import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMissionUseCase {
  private final MissionRepository missionRepository;
  private final UserRepository userRepository;

  @Transactional
  public void execute(CreateMissionCommand command) {
    final int pageSize = 1000;
    int page = 0;
    Page<User> chunk;

    do {
      chunk = userRepository.findAll(PageRequest.of(page, pageSize));
      if (chunk.isEmpty()) break;

      List<String> userIds = chunk.getContent().stream().map(User::getId).toList();

      LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
      LocalDateTime endOfDay = startOfDay.plusDays(1);

      List<String> alreadyHasToday =
          missionRepository.findUserIdsHavingContentToday(
              userIds, command.content(), startOfDay, endOfDay);

      Set<String> skip = new HashSet<>(alreadyHasToday);

      List<Mission> missionsToCreate =
          userIds.stream()
              .filter(uid -> !skip.contains(uid))
              .map(uid -> Mission.from(command, uid))
              .toList();

      if (!missionsToCreate.isEmpty()) {
        missionRepository.saveAll(missionsToCreate);
      }

      page++;
    } while (!chunk.isLast());
  }
}
