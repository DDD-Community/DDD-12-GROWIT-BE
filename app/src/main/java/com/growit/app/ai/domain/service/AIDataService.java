package com.growit.app.ai.domain.service;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIDataService {

    private final GoalRepository goalRepository;
    private final ToDoRepository toDoRepository;
    private final RetrospectRepository retrospectRepository;

    public AIDataResponse getDataForAdvice(String userId) {
        Optional<Goal> currentGoal = goalRepository.findByUserIdAndGoalDuration(userId, LocalDate.now());

        if (currentGoal.isEmpty()) {
            log.warn("No active goal found for user: {}", userId);
            return AIDataResponse.empty();
        }

        Goal goal = currentGoal.get();
        String goalId = goal.getId();

        List<ToDo> recentTodos = toDoRepository.findByGoalId(goalId);
        List<Retrospect> weeklyRetrospects = retrospectRepository.findByGoalIdAndUserId(goalId, userId);

        List<String> recentTodoContents = recentTodos.stream()
                .filter(todo -> !todo.isCompleted())
                .map(ToDo::getContent)
                .toList();

        List<String> completedTodoContents = recentTodos.stream()
                .filter(ToDo::isCompleted)
                .map(ToDo::getContent)
                .toList();

        List<String> retrospectContents = weeklyRetrospects.stream()
                .map(Retrospect::getContent)
                .toList();

        List<String> pastWeeklyGoals = goal.getPlans().stream()
                .map(plan -> plan.getContent())
                .toList();

        return AIDataResponse.builder()
                .recentTodos(recentTodoContents)
                .weeklyRetrospects(retrospectContents)
                .overallGoal(goal.getName())
                .completedTodos(completedTodoContents)
                .incompleteTodos(recentTodoContents)
                .pastWeeklyGoals(pastWeeklyGoals)
                .build();
    }

    public AIDataResponse getDataForPlanRecommendation(String userId, String goalId) {
        Optional<Goal> goal = goalRepository.findByIdAndUserId(goalId, userId);

        if (goal.isEmpty()) {
            log.warn("Goal not found: goalId={}, userId={}", goalId, userId);
            return AIDataResponse.empty();
        }

        Goal goalData = goal.get();
        List<ToDo> todos = toDoRepository.findByGoalId(goalId);
        List<Retrospect> retrospects = retrospectRepository.findByGoalIdAndUserId(goalId, userId);

        List<String> completedTodos = todos.stream()
                .filter(ToDo::isCompleted)
                .map(ToDo::getContent)
                .toList();

        List<String> incompleteTodos = todos.stream()
                .filter(todo -> !todo.isCompleted())
                .map(ToDo::getContent)
                .toList();

        List<String> pastRetrospects = retrospects.stream()
                .map(Retrospect::getContent)
                .toList();

        List<String> pastWeeklyGoals = goalData.getPlans().stream()
                .map(plan -> plan.getContent())
                .toList();

        String remainingTime = calculateRemainingTime(goalData.getDuration().endDate());

        return AIDataResponse.builder()
                .recentTodos(incompleteTodos)
                .weeklyRetrospects(pastRetrospects)
                .overallGoal(goalData.getName())
                .completedTodos(completedTodos)
                .incompleteTodos(incompleteTodos)
                .pastWeeklyGoals(pastWeeklyGoals)
                .remainingTime(remainingTime)
                .build();
    }

    private String calculateRemainingTime(LocalDate endDate) {
        LocalDate today = LocalDate.now();
        long daysRemaining = ChronoUnit.DAYS.between(today, endDate);

        if (daysRemaining <= 0) {
            return "목표 기간 종료";
        } else if (daysRemaining < 7) {
            return daysRemaining + "일";
        } else if (daysRemaining < 30) {
            long weeks = daysRemaining / 7;
            return weeks + "주";
        } else {
            long months = daysRemaining / 30;
            return months + "개월";
        }
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AIDataResponse {
        private List<String> recentTodos;
        private List<String> weeklyRetrospects;
        private String overallGoal;
        private List<String> completedTodos;
        private List<String> incompleteTodos;
        private List<String> pastWeeklyGoals;
        private String remainingTime;

        public static AIDataResponse empty() {
            return AIDataResponse.builder()
                    .recentTodos(List.of())
                    .weeklyRetrospects(List.of())
                    .overallGoal("")
                    .completedTodos(List.of())
                    .incompleteTodos(List.of())
                    .pastWeeklyGoals(List.of())
                    .remainingTime("")
                    .build();
        }
    }
}
