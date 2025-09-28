package com.growit.app.advice.usecase;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.user.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GenerateMentorAdviceUseCase {

  private final AiMentorAdviceClient aiMentorAdviceClient;
  private final GetUserGoalsUseCase getUserGoalsUseCase;
  private final ToDoRepository toDoRepository;
  private final GoalRetrospectRepository goalRetrospectRepository;

  public MentorAdvice execute(User user) {
    Goal currentGoal =
        getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("진행중인 목표가 없습니다."));

    LocalDate today = LocalDate.now();
    LocalDate aWeekAgo = today.minusWeeks(1);

    List<ToDo> weeklyTodos =
        toDoRepository.findAllByUserIdAndCreatedAtBetween(
            user.getId(), aWeekAgo.atStartOfDay(), today.atTime(23, 59, 59));

    List<String> completedTodos =
        weeklyTodos.stream()
            .filter(ToDo::isCompleted)
            .map(ToDo::getContent)
            .collect(Collectors.toList());

    List<String> incompleteTodos =
        weeklyTodos.stream()
            .filter(todo -> !todo.isCompleted())
            .map(ToDo::getContent)
            .collect(Collectors.toList());

    List<String> weeklyRetrospects =
        goalRetrospectRepository
            .findAllByGoalIdAndCreatedAtBetween(
                currentGoal.getId(), aWeekAgo.atStartOfDay(), today.atTime(23, 59, 59))
            .stream()
            .map(GoalRetrospect::getContent)
            .collect(Collectors.toList());

    AiMentorAdviceRequest.Input input =
        AiMentorAdviceRequest.Input.builder()
            .recentTodos(incompleteTodos)
            .weeklyRetrospects(weeklyRetrospects)
            .overallGoal(currentGoal.getName())
            .completedTodos(completedTodos)
            .incompleteTodos(incompleteTodos)
            .pastWeeklyGoals(
                currentGoal.getPlans().stream().map(Plan::getContent).collect(Collectors.toList()))
            .build();

    // teamcook-advice-001, confucius-advice-001, warren-buffett-advice-001
    String promptId = currentGoal.getMentor().getAdvicePromptId();

    AiMentorAdviceRequest request =
        AiMentorAdviceRequest.builder()
            .userId(user.getId())
            .promptId(promptId)
            .input(input)
            .build();

    AiMentorAdviceResponse response = aiMentorAdviceClient.getMentorAdvice(request);

    return MentorAdvice.builder()
        .id(IDGenerator.generateId())
        .userId(user.getId())
        .goalId(currentGoal.getId())
        .isChecked(false)
        .message("AI Mentor's Advice")
        .kpt(
            new MentorAdvice.Kpt(
                response.getOutput().getKeep(),
                response.getOutput().getProblem(),
                response.getOutput().getTryNext()))
        .build();
  }
}
