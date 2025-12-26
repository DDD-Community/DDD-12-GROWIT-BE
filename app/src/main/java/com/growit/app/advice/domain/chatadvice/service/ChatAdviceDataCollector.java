package com.growit.app.advice.domain.chatadvice.service;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.user.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** ChatAdvice 생성에 필요한 데이터를 수집하는 서비스 */
@Service
@RequiredArgsConstructor
public class ChatAdviceDataCollector {

  private final GoalRepository goalRepository;
  private final ToDoRepository toDoRepository;
  private final ChatAdviceRepository chatAdviceRepository;

  public RealtimeAdviceData collectRealtimeData(User user, String goalId, String userMessage) {
    // 선택한 목표 조회
    Goal selectedGoal =
        goalRepository
            .findById(goalId)
            .orElseThrow(() -> new NotFoundException("목표를 찾을 수 없습니다: " + goalId));

    // 최근 7일 투두 리스트 조회
    LocalDate today = LocalDate.now();
    LocalDate sevenDaysAgo = today.minusDays(7);
    List<ToDo> recentTodos =
        toDoRepository.findAllByUserIdAndCreatedAtBetween(
            user.getId(), sevenDaysAgo.atStartOfDay(), today.atTime(23, 59, 59));

    List<String> recentTodoContents =
        recentTodos.stream().map(ToDo::getContent).collect(Collectors.toList());

    return RealtimeAdviceData.builder()
        .userMessage(userMessage)
        .goalId(selectedGoal.getId())
        .selectedGoal(selectedGoal.getName())
        .recentTodos(recentTodoContents)
        .build();
  }

  public MorningAdviceData collectMorningAdviceData(User user) {
    // 1. 진행 중인 전체 목표 조회
    List<Goal> activeGoals = goalRepository.findAllByUserId(user.getId());
    List<String> activeGoalTitles =
        activeGoals.stream().map(Goal::getName).collect(Collectors.toList());

    // 2. 최근 7일 투두 리스트 조회
    LocalDate today = LocalDate.now();
    LocalDate sevenDaysAgo = today.minusDays(7);
    List<ToDo> recentTodos =
        toDoRepository.findAllByUserIdAndCreatedAtBetween(
            user.getId(), sevenDaysAgo.atStartOfDay(), today.atTime(23, 59, 59));
    List<String> recentTodoContents =
        recentTodos.stream().map(ToDo::getContent).collect(Collectors.toList());

    // 3. 어제 대화 내역 조회
    LocalDate yesterday = today.minusDays(1);

    ChatAdvice chatAdvice = chatAdviceRepository.findByUserId(user.getId()).orElse(null);
    String yesterdayConversation = "";

    if (chatAdvice != null && chatAdvice.getConversations() != null) {
      yesterdayConversation =
          chatAdvice.getConversations().stream()
              .filter(c -> c.getTimestamp().toLocalDate().equals(yesterday))
              .filter(c -> !c.isOnboarding()) // Filter out onboarding messages
              .map(c -> "User: " + c.getUserMessage() + "\nAI: " + c.getGrorongResponse())
              .collect(Collectors.joining("\n"));
    }

    return MorningAdviceData.builder()
        .userId(user.getId())
        .activeGoals(activeGoalTitles)
        .recentTodos(recentTodoContents)
        .yesterdayConversation(yesterdayConversation)
        .build();
  }

  @Builder
  @Getter
  public static class RealtimeAdviceData {
    private String userMessage;
    private String goalId;
    private String selectedGoal;
    private List<String> recentTodos;
  }

  @Builder
  @Getter
  public static class MorningAdviceData {
    private String userId;
    private List<String> activeGoals;
    private List<String> recentTodos;
    private String yesterdayConversation;
  }
}
