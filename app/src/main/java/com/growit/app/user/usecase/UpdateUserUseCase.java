package com.growit.app.user.usecase;

import com.growit.app.advice.domain.saju.service.SajuFortuneClient;
import com.growit.app.resource.domain.jobrole.service.JobRoleValidator;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.UpdateUserCommand;
import com.growit.app.user.domain.user.vo.SajuInfo;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateUserUseCase {
  private final JobRoleValidator jobRoleValidator;
  private final UserRepository userRepository;
  private final SajuFortuneClient sajuFortuneClient;

  @Transactional
  public void execute(UpdateUserCommand command) {
    jobRoleValidator.checkJobRoleExist(command.jobRoleId());

    final User user = command.user();

    user.updateByCommand(command);

    if (command.sajuInfo() != null) {
      SajuInfo sajuInfo = command.sajuInfo();
      Map<String, String> manse =
          sajuFortuneClient.getManse(
              sajuInfo.birth().toString(),
              sajuInfo.birthHour().getStart(),
              sajuInfo.gender().name());

      SajuInfo newSajuInfo =
          new SajuInfo(
              sajuInfo.gender(),
              sajuInfo.birth(),
              sajuInfo.birthHour(),
              manse.get("year"),
              manse.get("month"),
              manse.get("day"),
              manse.get("time"));

      user.updateSaju(newSajuInfo);
    }



    userRepository.saveUser(user);
  }

  @Transactional
  public void isOnboarding(User user) {
    user.onboarding();
    userRepository.saveUser(user);
  }
}
