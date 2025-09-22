package com.growit.app.aikpts.controller.mapper;

import com.growit.app.aikpts.controller.dto.response.AIKPTSResponse;
import com.growit.app.aikpts.domain.aikpts.AIKPTS;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AIKPTSMapper {

  public AIKPTSResponse toResponse(AIKPTS aikpts) {
    return AIKPTSResponse.builder()
        .id(aikpts.getId())
        .aiAdviceId(aikpts.getAiAdviceId())
        .keeps(aikpts.getKpt().getKeeps())
        .problems(aikpts.getKpt().getProblems())
        .trys(aikpts.getKpt().getTrys())
        .createdAt(aikpts.getCreatedAt())
        .updatedAt(aikpts.getUpdatedAt())
        .build();
  }

  public List<AIKPTSResponse> toResponseList(List<AIKPTS> aikptsList) {
    return aikptsList.stream()
        .map(this::toResponse)
        .toList();
  }
}
