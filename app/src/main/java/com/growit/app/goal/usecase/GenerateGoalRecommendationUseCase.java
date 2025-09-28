package com.growit.app.goal.usecase;

import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationRequest;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationResponse;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendationRepository;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.user.domain.user.User;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GenerateGoalRecommendationUseCase {

  private final AiMentorAdviceClient aiMentorAdviceClient;
  private final GetUserGoalsUseCase getUserGoalsUseCase;
  private final ToDoRepository toDoRepository;
  private final GoalRetrospectRepository goalRetrospectRepository;
  private final PlanRecommendationRepository planRecommendationRepository;

  public PlanRecommendation execute(User user) {
    Goal currentGoal =
        getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("진행중인 목표가 없습니다."));

    LocalDate today = LocalDate.now();
    LocalDate aMonthAgo = today.minusMonths(1);

    List<ToDo> monthlyTodos =
        toDoRepository.findAllByUserIdAndCreatedAtBetween(
            user.getId(), aMonthAgo.atStartOfDay(), today.atTime(23, 59, 59));

    List<String> pastTodos =
        monthlyTodos.stream().map(ToDo::getContent).collect(Collectors.toList());

    List<String> completedTodos =
        monthlyTodos.stream()
            .filter(ToDo::isCompleted)
            .map(ToDo::getContent)
            .collect(Collectors.toList());

    List<String> pastRetrospects =
        goalRetrospectRepository
            .findAllByGoalIdAndCreatedAtBetween(
                currentGoal.getId(), aMonthAgo.atStartOfDay(), today.atTime(23, 59, 59))
            .stream()
            .map(GoalRetrospect::getContent)
            .collect(Collectors.toList());

    if (pastRetrospects.isEmpty()) {
      pastRetrospects.add("");
    }

    String remainingTime = calculateRemainingTime(currentGoal);

    AiGoalRecommendationRequest.Input input =
        AiGoalRecommendationRequest.Input.builder()
            .pastTodos(pastTodos)
            .pastRetrospects(pastRetrospects)
            .overallGoal(currentGoal.getName())
            .completedTodos(completedTodos)
            .pastWeeklyGoals(
                currentGoal.getPlans().stream()
                    .map(Plan::getContent)
                    .filter(content -> content != null && !content.isBlank())
                    .collect(Collectors.toList()))
            .remainingTime(remainingTime)
            .build();

    AiGoalRecommendationRequest request =
        AiGoalRecommendationRequest.builder()
            .userId(user.getId())
            .promptId(currentGoal.getMentor().getGoalPromprtId())
            .input(input)
            .build();

    AiGoalRecommendationResponse response = aiMentorAdviceClient.getGoalRecommendation(request);

    // 현재 주차의 계획을 찾거나, 없으면 첫 번째 계획을 사용
    Plan targetPlan = currentGoal.getPlans().stream()
        .filter(Plan::isCurrentWeek)
        .findFirst()
        .orElse(currentGoal.getPlans().get(0));

    PlanRecommendation planRecommendation =
        new PlanRecommendation(
            IDGenerator.generateId(),
            user.getId(),
            currentGoal.getId(),
            targetPlan.getId(),
            response.getOutput());

    planRecommendationRepository.save(planRecommendation);

    return planRecommendation;
  }

  private String calculateRemainingTime(Goal goal) {
    LocalDate today = LocalDate.now();
    LocalDate dueDate = goal.getDuration().endDate();

    if (dueDate == null) {
      return "기간 정보 없음";
    }

    if (today.isAfter(dueDate)) {
      return "목표 기간 종료";
    }

    Period period = Period.between(today, dueDate);
    int months = period.getMonths();
    int days = period.getDays();

    if (months > 0 && days > 0) {
      return String.format("%d개월 %d일 남음", months, days);
    } else if (months > 0) {
      return String.format("%d개월 남음", months);
    } else if (days > 0) {
      return String.format("%d일 남음", days);
    } else {
      return "오늘 마감";
    }
  }
}
