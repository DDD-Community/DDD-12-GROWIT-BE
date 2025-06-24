package com.growit.app.goal.domain.goal.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GoalService implements GoalValidator{
  @Override
  public void checkPlans(GoalDuration duration, List<PlanDto> plans) throws BadRequestException {
    long weeks = duration.getWeekCount();
    if(weeks != plans.size()) {
      throw new BadRequestException("설정한 날짜 범위와 주간 계획수가 일치하지 않습니다.");
    }
  }
}
