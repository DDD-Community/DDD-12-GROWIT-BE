package com.growit.app.advice.domain.mentor.service;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.common.util.IDGenerator;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class MockMentorService implements MentorService {

  private static final List<String> MOCK_MESSAGES =
      List.of(
          "혁신은 단순함에서 시작돼", "완벽함보다 실행이 더 중요해", "실패는 성공의 어머니야", "꾸준함이 천재를 이긴다", "작은 변화가 큰 결과를 만들어");

  private static final List<MentorAdvice.Kpt> MOCK_KPTS =
      List.of(
          new MentorAdvice.Kpt(
              "투두 분해와 실행 리듬은 안정적이다.", "디자인 - 개발 QA 협업 속도가 더디다", "이번주는 MVP를 반드시 배포해라."),
          new MentorAdvice.Kpt("목표 설정과 계획 수립이 체계적이다.", "중간 점검과 피드백이 부족하다", "주간 회고를 통해 개선점을 찾아라."),
          new MentorAdvice.Kpt("학습 의욕과 성장 마인드가 뛰어나다", "시간 관리와 우선순위 설정이 아쉽다", "가장 중요한 3가지에 집중하라"));

  private final Random random = new Random();

  @Override
  public MentorAdvice getMentorAdvice(String userId, String goalId) {
    String mockMessage = MOCK_MESSAGES.get(random.nextInt(MOCK_MESSAGES.size()));
    MentorAdvice.Kpt mockKpt = MOCK_KPTS.get(random.nextInt(MOCK_KPTS.size()));

    return new MentorAdvice(
        IDGenerator.generateId(),
        userId, // userId는 나중에 설정됨
        goalId,
        false, // 새로 생성된 조언은 미확인 상태
        mockMessage,
        mockKpt);
  }
}
