package com.growit.app.goal.domain.goal.planet;

public interface PlanetAssignmentService {
  /**
   * 순환 방식으로 다음 행성을 할당합니다. 마지막 생성된 목표의 행성 ID보다 1개 더 큰 값으로 할당하며, 행성이 20개면 21번째는 다시 첫 번째 행성으로 할당됩니다.
   */
  Planet getNextPlanet(String userId);
}
