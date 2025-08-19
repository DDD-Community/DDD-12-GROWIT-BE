package com.growit.app.mission.usecase;

import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import java.util.List;
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

      List<Mission> missions =
          chunk.getContent().stream().map(u -> Mission.from(command, u.getId())).toList();

      missionRepository.saveAll(missions);
      page++;
    } while (!chunk.isLast());
  }
}
