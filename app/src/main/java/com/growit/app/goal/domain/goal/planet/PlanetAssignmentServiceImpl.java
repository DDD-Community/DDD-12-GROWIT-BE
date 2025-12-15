package com.growit.app.goal.domain.goal.planet;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanetAssignmentServiceImpl implements PlanetAssignmentService {

  private final GoalRepository goalRepository;
  private final PlanetRepository planetRepository;

  @Override
  public Planet getNextPlanet(String userId) {
    // 모든 행성 목록 조회 (ID 순으로 정렬)
    List<Planet> allPlanets = planetRepository.findAll();

    // 가장 최근에 생성된 목표의 행성 ID 조회
    Optional<Goal> lastGoal = goalRepository.findLastGoal(userId);

    if (lastGoal.isEmpty()) {
      // 첫 번째 목표라면 첫 번째 행성 반환
      return allPlanets.get(0);
    }

    // 마지막 목표의 행성 ID 찾기
    Long lastPlanetId = lastGoal.get().getPlanet().id();

    // 다음 행성 ID 계산 (순환)
    Long nextPlanetId = getNextPlanetId(lastPlanetId, allPlanets);

    // 다음 행성 엔티티 찾기
    // 찾지 못하면 첫 번째 행성
    return allPlanets.stream()
        .filter(planet -> planet.id().equals(nextPlanetId))
        .findFirst()
        .orElse(allPlanets.get(0));
  }

  private Long getNextPlanetId(Long lastPlanetId, List<Planet> allPlanets) {
    // 현재 행성의 인덱스 찾기
    int currentIndex = -1;
    for (int i = 0; i < allPlanets.size(); i++) {
      if (allPlanets.get(i).id().equals(lastPlanetId)) {
        currentIndex = i;
        break;
      }
    }

    // 다음 인덱스 계산 (순환)
    int nextIndex = (currentIndex + 1) % allPlanets.size();
    return allPlanets.get(nextIndex).id();
  }
}
